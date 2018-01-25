/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.structure.general.expression.callbacks.invoker;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.general.argument.ArgumentParser;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionCallbackParser;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.prototype.structure.method.invoker.MethodInvoker;

public class MethodInvokerExpressionParser implements ExpressionCallbackParser<MethodInvokerExpressionCallback> {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .build();

    protected static final TokenPattern CALL_PATTERN = TokenPattern.builder()
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .build();

    private final TokenizedSource instanceSource;
    private final TokenizedSource methodNameSource;
    private final TokenizedSource argumentsSource;
    private MethodInvoker invoker;

    public MethodInvokerExpressionParser(TokenizedSource instanceSource, TokenizedSource methodNameSource, TokenizedSource argumentsSource) {
        this.instanceSource = instanceSource;
        this.methodNameSource = methodNameSource;
        this.argumentsSource = argumentsSource;
    }

    @Override
    public void parse(TokenizedSource source, ParserInfo info) {
        PandaScript script = info.getComponent(Components.SCRIPT);
        ImportRegistry registry = script.getImportRegistry();

        Expression instance = null;
        ClassPrototype prototype;

        if (instanceSource != null) {
            String surmiseClassName = instanceSource.asString();
            prototype = registry.forClass(surmiseClassName);

            if (prototype == null) {
                ExpressionParser expressionParser = new ExpressionParser();

                instance = expressionParser.parse(info, instanceSource);
                prototype = instance.getReturnType();
            }
        }
        else {
            prototype = info.getComponent(Components.CLASS_PROTOTYPE);
            instance = new Expression(prototype, new ThisExpressionCallback());
        }

        ArgumentParser argumentParser = new ArgumentParser();
        Expression[] arguments = argumentParser.parse(info, argumentsSource);

        String methodName = methodNameSource.asString();
        Method prototypeMethod = prototype.getMethods().getMethod(methodName);

        if (prototypeMethod == null) {
            throw new PandaParserException("Class " + prototype.getClassName() + " does not have method " + methodName);
        }

        if (prototypeMethod.isVoid()) {
            throw new PandaParserException("Method " + prototypeMethod.getMethodName() + " returns nothing [" + source.getLast().getLine() + "]");
        }

        this.invoker = new MethodInvoker(prototypeMethod, instance, arguments);
    }

    @Override
    public MethodInvokerExpressionCallback toCallback() {
        return new MethodInvokerExpressionCallback(invoker);
    }

}
