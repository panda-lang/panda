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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.invoker;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.method.invoker.MethodInvoker;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionUtils;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks.ThisExpressionCallback;
import org.panda_lang.panda.framework.language.resource.parsers.common.ArgumentsParser;

public class MethodInvokerExpressionParser {

    private static final ArgumentsParser ARGUMENT_PARSER = new ArgumentsParser();

    private final Snippet instanceSource;
    private final String methodName;
    private final Snippet argumentsSource;
    private MethodInvoker invoker;

    public MethodInvokerExpressionParser(Snippet instanceSource, String methodName, Snippet argumentsSource) {
        this.instanceSource = instanceSource;
        this.methodName = methodName;
        this.argumentsSource = argumentsSource;
    }

    public MethodInvokerExpressionParser(Snippet instanceSource, Snippet methodNameSource, Snippet argumentsSource) {
        this(instanceSource, methodNameSource.asString(), argumentsSource);
    }

    public MethodInvokerExpressionParser() {
        this(null, (String) null, null);
    }

    public void parse(@Nullable Snippet source, ParserData data) {
        Expression instance = instanceSource != null ? data.getComponent(UniversalComponents.EXPRESSION).parse(data, instanceSource) : ThisExpressionCallback.of(data);
        parse(data, instance, methodName, argumentsSource);
    }

    public void parse(ParserData data, Expression instance, String methodName, Snippet argumentsSource) {
        ClassPrototype prototype = instance.getReturnType();

        Expression[] arguments = ARGUMENT_PARSER.parse(data, argumentsSource);
        ClassPrototype[] argumentTypes = ExpressionUtils.toTypes(arguments);
        PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName, argumentTypes);

        if (prototypeMethod == null) {
            throw new PandaParserFailure("Class " + prototype.getClassName() + " does not have method with these parameters" + methodName, data, argumentsSource);
        }

        this.invoker = new MethodInvoker(prototypeMethod, instance, arguments);
    }

    public MethodInvoker getInvoker() {
        return invoker;
    }

    public MethodInvokerExpressionCallback toCallback() {
        return new MethodInvokerExpressionCallback(invoker);
    }

}
