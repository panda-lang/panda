/*
 * Copyright (c) 2020 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.shell.repl;

import io.vavr.control.Option;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Statement;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.interpreter.source.PandaSource;
import org.panda_lang.framework.language.interpreter.token.PandaLocation;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.framework.language.runtime.PandaProcessFailure;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.framework.language.runtime.PandaRuntimeConstants;
import org.panda_lang.panda.shell.repl.ReplResult.Type;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Interactive shell - REPL (read-eval-print loop)
 */
public final class Repl {

    private final ReplConsole console;
    private final Context context;
    private final ExpressionParser expressionParser;
    private final Supplier<Process> processSupplier;
    private final ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier;
    private final boolean customExceptionListener;
    private ReplExceptionListener exceptionListener;
    private ProcessStack stack;
    private Frame instance;
    private StringBuilder history;

    Repl(ReplCreator creator) throws Exception {
        this.console = creator.console;
        this.context = creator.context;
        this.expressionParser = context.getComponent(Components.EXPRESSION);
        this.processSupplier = creator.processSupplier;
        this.instanceSupplier = creator.instanceSupplier;
        this.exceptionListener = creator.exceptionListener;
        this.customExceptionListener = exceptionListener != null;
        this.regenerate();
    }

    /**
     * Regenerate REPL and cleanup cache
     *
     * @throws Exception if something happen
     */
    public void regenerate() throws Exception {
        this.history = new StringBuilder();
        this.stack = new PandaProcessStack(processSupplier.get(), PandaRuntimeConstants.DEFAULT_STACK_SIZE);

        this.instance = context.getComponent(Components.SCOPE).getStandardizedFramedScope().getOrElseThrow(() -> {
            throw new PandaFrameworkException("Required scope has to be standardized");
        }).revive(stack, instanceSupplier.apply(stack));

        if (!customExceptionListener) {
            this.exceptionListener = (exception, runtime) -> {
                context.getComponent(Components.ENVIRONMENT).getMessenger().send(runtime ? new PandaProcessFailure(stack, exception) : exception);
            };
        }
    }

    /**
     * Evaluate the given source
     *
     * @param source the source to evaluate
     * @return collection of repl results
     */
    public Collection<ReplResult> evaluate(String source) {
        return evaluateCommand(source)
                .map(Collections::singletonList)
                .getOrElse(() -> evaluateSource(source));
    }

    private Option<ReplResult> evaluateCommand(String command) {
        String content = command.toLowerCase().trim();

        if (content.isEmpty()) {
            content = "help";
        }

        Object result = "-- " + content + System.lineSeparator();

        switch (content) {
            case "vars": {
                StringBuilder variables = new StringBuilder();

                for (Variable variable : instance.getFramedScope().getVariables()) {
                    variables.append(variable.getName()).append(": ").append((Object) instance.get(variable.getPointer())).append(System.lineSeparator());
                }

                result += variables.toString();
                break;
            }
            case "history": {
                result += history.toString();
                break;
            }
            case "exit": {
                console.interrupt();
                break;
            }
            case "?":
            case "help": {
                result += "Default help page of REPL. All the available commands: \n";
                result += "  help - display help page \n";
                result += "  vars - display all the registered variables in REPL scope \n";
                result += "  history - display history of evaluated snippets \n";
                result += "  exit - exit shell \n";
                break;
            }
            default:
                return Option.none();
        }

        return Option.of(new ReplResult(Type.SHELL, result));
    }

    private List<ReplResult> evaluateSource(String source) {
        if (source.trim().isEmpty()) {
            return Collections.emptyList();
        }

        Snippet[] expressions = PandaLexerUtils.convert("REPL source", source).split(Separators.SEMICOLON);
        List<ReplResult> collection = new ArrayList<>(expressions.length);

        for (Snippet expressionSource : expressions) {
            ReplResult result = evaluateExpression(expressionSource);

            if (result.getType() == null) {
                continue;
            }

            collection.add(result);
            history.append(expressionSource.toString()).append(System.lineSeparator());
        }

        return collection;
    }

    private ReplResult evaluateExpression(Snippet expressionSource) {
        context.withComponent(Components.CURRENT_SOURCE, expressionSource);
        Expression expression;

        try {
            expression = expressionParser.parse(context, expressionSource).getExpression();
        } catch (Exception e) {
            exceptionListener.onException(e, false);
            return ReplResult.NONE;
        }

        try {
            Location location = new PandaLocation(new PandaSource("REPL source", history.toString(), true), 0, 0);
            Statement statement = new ReplStatement(location, expression);

            Result<?> result = stack.callCustomFrame(instance, instance, () -> {
                return stack.callStatement(instance, statement);
            });

            if (result == null) {
                return ReplResult.NONE;
            }

            return new ReplResult(Type.PANDA, result.getResult());
        } catch (Exception e) {
            exceptionListener.onException(e, true);
            return ReplResult.NONE;
        }
    }

    /**
     * Initiate REPL creator
     *
     * @param console the console instance to use
     * @return a REPL creator instance
     */
    public static ReplCreator creator(ReplConsole console) {
        return new ReplCreator(console);
    }

}
