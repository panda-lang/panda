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

import org.panda_lang.framework.design.architecture.dynamic.Frame;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.runtime.Process;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.runtime.Result;
import org.panda_lang.framework.design.runtime.Status;
import org.panda_lang.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.framework.language.runtime.PandaProcessStack;
import org.panda_lang.framework.language.runtime.PandaProcessStackConstants;
import org.panda_lang.panda.Panda;
import org.panda_lang.utilities.commons.function.ThrowingFunction;

import java.util.function.Supplier;

public final class Shell {

    private final Context context;
    private final ExpressionParser expressionParser;
    private final Supplier<Process> processSupplier;
    private final ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier;
    private ProcessStack stack;
    private Frame instance;

    Shell(Context context, ExpressionParser expressionParser, Supplier<Process> processSupplier, ThrowingFunction<ProcessStack, Object, Exception> instanceSupplier) throws Exception {
        this.context = context;
        this.expressionParser = expressionParser;
        this.processSupplier = processSupplier;
        this.instanceSupplier = instanceSupplier;
        this.regenerate();
    }

    public ShellResult evaluate(String source) throws Exception {
        Snippet expressionSource = PandaLexerUtils.convert(source);

        context.withComponent(Components.CURRENT_SOURCE, expressionSource);
        Expression expression = expressionParser.parse(context, expressionSource).getExpression();

        Result<?> result = stack.call(instance, instance, () -> {
            return new Result<>(Status.RETURN, expression.evaluate(stack, instance));
        });

        return new ShellResult(result != null ? result.getResult() : null);
    }

    public void regenerate() throws Exception {
        this.stack = new PandaProcessStack(processSupplier.get(), PandaProcessStackConstants.DEFAULT_STACK_SIZE);
        this.instance = context.getComponent(Components.SCOPE).getScope().revive(stack, instanceSupplier.apply(stack));
    }

    public static ShellCreator creator(Panda panda) {
        return new ShellCreator(panda);
    }

}
