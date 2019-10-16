/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.cli.shell;

import org.panda_lang.framework.design.FrameworkController;
import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.design.runtime.Status;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.framework.language.runtime.PandaProcessStackConstants;
import org.panda_lang.panda.cli.shell.ShellResult.Type;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

/**
 * Interactive shell
 */
public final class Shell {

    private final Context context;
    private final ExpressionParser expressionParser;
    private final Supplier<Process> processSupplier;
    private final ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier;
    private StringBuilder history;
    private ProcessStack stack;
    private Frame instance;

    Shell(Context context, ExpressionParser expressionParser, Supplier<Process> processSupplier, ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier) throws Exception {
        this.context = context;
        this.expressionParser = expressionParser;
        this.processSupplier = processSupplier;
        this.instanceSupplier = instanceSupplier;
        this.regenerate();
    }

    /**
     * Regenerate shell and cleanup cache
     *
     * @throws Exception if something happen
     */
    public void regenerate() throws Exception {
        this.history = new StringBuilder();
        this.stack = new PandaProcessStack(processSupplier.get(), PandaProcessStackConstants.DEFAULT_STACK_SIZE);
        this.instance = context.getComponent(Components.SCOPE).getScope().revive(stack, instanceSupplier.apply(stack));
    }

    /**
     * Evaluate the given source
     *
     * @param source the source to evaluate
     * @return collection of shell results
     * @throws Exception if something happen
     */
    public Collection<ShellResult> evaluate(String source) throws Exception {
        return source.startsWith("!!") ? Collections.singletonList(evaluateCommand(source)) : evaluateSource(source);
    }

    private ShellResult evaluateCommand(String command) {
        String content = command.substring(2).toLowerCase().trim();
        Object result = "-- " + content + System.lineSeparator();

        if (content.equals("vars")) {
            StringBuilder variables = new StringBuilder();

            for (Variable variable : instance.getScope().getVariables()) {
                variables.append(variable.getName()).append(": ").append((Object) instance.get(variable.getPointer())).append(System.lineSeparator());
            }

            result += variables.toString();
        }
        else if (content.equals("history")) {
            result += history.toString();
        }

        return new ShellResult(Type.SHELL, result);
    }

    private Collection<ShellResult> evaluateSource(String source) throws Exception {
        Snippet[] expressions = PandaLexerUtils.convert(source).split(Separators.SEMICOLON);
        Collection<ShellResult> collection = new ArrayList<>(expressions.length);

        for (Snippet expressionSource : expressions) {
            collection.add(evaluateExpression(expressionSource));
            history.append(expressionSource.toString()).append(System.lineSeparator());
        }

        return collection;
    }

    private ShellResult evaluateExpression(Snippet expressionSource) throws Exception {
        context.withComponent(Components.CURRENT_SOURCE, expressionSource);
        Expression expression = expressionParser.parse(context, expressionSource).getExpression();

        Result<?> result = stack.call(instance, instance, () -> {
            return new Result<>(Status.RETURN, expression.evaluate(stack, instance));
        });

        return new ShellResult(Type.PANDA, result != null ? result.getResult() : null);
    }

    /**
     * Initiate shell creator
     *
     * @param frameworkController the framework controller that contains required resources
     * @return a shell creator instance
     */
    public static ShellCreator creator(FrameworkController frameworkController) {
        return new ShellCreator(frameworkController);
    }

}
