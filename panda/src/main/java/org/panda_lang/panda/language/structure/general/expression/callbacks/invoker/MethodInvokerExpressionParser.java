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

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
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
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.prototype.structure.method.invoker.MethodInvoker;

import java.util.List;

public class MethodInvokerExpressionParser implements ExpressionCallbackParser<MethodInvokerExpressionCallback> {

    public static final TokenPattern PATTERN = TokenPattern.builder()
            .hollow()
            .unit(TokenType.SEPARATOR, ".")
            .simpleHollow()
            .unit(TokenType.SEPARATOR, "(")
            .hollow()
            .unit(TokenType.SEPARATOR, ")")
            .build();

    private final List<TokenizedSource> methodMatches;
    private MethodInvoker invoker;

    public MethodInvokerExpressionParser(List<TokenizedSource> methodMatches) {
        this.methodMatches = methodMatches;
    }

    @Override
    public void parse(TokenizedSource source, ParserInfo info) {
        TokenPatternHollows hollows = new TokenPatternHollows(methodMatches);

        TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);
        redactor.map("instance", "method-name", "arguments");

        TokenizedSource instanceSource = redactor.get("instance");
        TokenizedSource methodSource = redactor.get("method-name");
        TokenizedSource argumentsSource = redactor.get("arguments");

        PandaScript script = info.getComponent(Components.SCRIPT);
        ImportRegistry registry = script.getImportRegistry();

        String surmiseClassName = TokenizedSource.asString(instanceSource);
        ClassPrototype prototype = registry.forClass(surmiseClassName);

        String methodName = TokenizedSource.asString(methodSource);
        Expression instance = null;

        if (prototype == null) {
            ExpressionParser expressionParser = new ExpressionParser();

            instance = expressionParser.parse(info, instanceSource);
            prototype = instance.getReturnType();
        }

        ArgumentParser argumentParser = new ArgumentParser();
        Expression[] arguments = argumentParser.parse(info, argumentsSource);

        Method prototypeMethod = prototype.getMethods().getMethod(methodName);
        this.invoker = new MethodInvoker(prototypeMethod, instance, arguments);

        if (this.invoker.getMethod().isVoid()) {
            throw new PandaParserException("Method " + prototypeMethod.getMethodName() + " returns nothing [" + source.getLast().getLine() + "]");
        }
    }

    @Override
    public MethodInvokerExpressionCallback toCallback() {
        return new MethodInvokerExpressionCallback(invoker);
    }

}
