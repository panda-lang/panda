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

package org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoaderUtils;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.architecture.prototype.method.invoker.MethodInvoker;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.gapped.GappedPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionCallbackParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.parsers.expression.xxx.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.general.ArgumentParser;
import org.panda_lang.panda.framework.language.resource.parsers.prototype.ClassPrototypeComponents;

public class MethodInvokerExpressionParser implements ExpressionCallbackParser<MethodInvokerExpressionCallback> {

    protected static final GappedPattern PATTERN = new GappedPatternBuilder()
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .lastIndexAlgorithm(true)
            .build();

    protected static final GappedPattern CALL_PATTERN = new GappedPatternBuilder()
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .lastIndexAlgorithm(true)
            .build();

    private final Snippet instanceSource;
    private final Snippet methodNameSource;
    private final Snippet argumentsSource;
    private MethodInvoker invoker;

    public MethodInvokerExpressionParser(Snippet instanceSource, Snippet methodNameSource, Snippet argumentsSource) {
        this.instanceSource = instanceSource;
        this.methodNameSource = methodNameSource;
        this.argumentsSource = argumentsSource;
    }

    @Override
    public void parse(@Nullable Snippet source, ParserData data) {
        Expression instance = null;
        ClassPrototype prototype;

        if (instanceSource != null) {
            ClassPrototypeReference reference = ModuleLoaderUtils.getReferenceOrNull(data, instanceSource.asString());

            if (reference == null) {
                instance = data.getComponent(PandaComponents.EXPRESSION).parse(data, instanceSource);
                prototype = instance.getReturnType();
            }
            else {
                prototype = reference.fetch();
            }
        }
        else {
            prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
            instance = ThisExpressionCallback.asExpression(prototype);
        }

        ArgumentParser argumentParser = new ArgumentParser();
        Expression[] arguments = argumentParser.parse(data, argumentsSource);
        ClassPrototype[] parameterTypes = ExpressionUtils.toTypes(arguments);

        String methodName = methodNameSource.asString();
        PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName, parameterTypes);

        if (prototypeMethod == null) {
            throw new PandaParserFailure("Class " + prototype.getClassName() + " does not have method " + methodName, data, source);
        }

        this.invoker = new MethodInvoker(prototypeMethod, instance, arguments);
    }

    public MethodInvoker getInvoker() {
        return invoker;
    }

    @Override
    public MethodInvokerExpressionCallback toCallback() {
        return new MethodInvokerExpressionCallback(invoker);
    }

}
