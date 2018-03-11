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

import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.design.interpreter.parser.util.Components;
import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.general.argument.ArgumentParser;
import org.panda_lang.panda.framework.design.architecture.detach.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionCallbackParser;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.ExpressionUtils;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.method.PrototypeMethod;
import org.panda_lang.panda.language.structure.statement.invoker.MethodInvoker;

public class MethodInvokerExpressionParser implements ExpressionCallbackParser<MethodInvokerExpressionCallback> {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .lastIndexAlgorithm(true)
            .build();

    protected static final AbyssPattern CALL_PATTERN = new AbyssPatternBuilder()
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .lastIndexAlgorithm(true)
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
        ClassPrototype[] parameterTypes = ExpressionUtils.toTypes(arguments);

        String methodName = methodNameSource.asString();
        PrototypeMethod prototypeMethod = prototype.getMethods().getMethod(methodName, parameterTypes);

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
