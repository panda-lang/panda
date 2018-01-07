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

package org.panda_lang.panda.language.structure.general.expression;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.value.PandaValue;
import org.panda_lang.panda.core.structure.value.Variable;
import org.panda_lang.panda.core.structure.wrapper.Scope;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.Parser;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.InstanceExpressionCallback;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.InstanceExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.language.structure.general.expression.callbacks.invoker.MethodInvokerExpressionCallback;
import org.panda_lang.panda.language.structure.general.expression.callbacks.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.callbacks.memory.FieldExpressionCallback;
import org.panda_lang.panda.language.structure.general.expression.callbacks.memory.VariableExpressionCallback;
import org.panda_lang.panda.language.structure.general.expression.callbacks.number.NumberUtils;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.field.Field;
import org.panda_lang.panda.language.structure.scope.variable.VariableParserUtils;
import org.panda_lang.panda.language.syntax.tokens.Separators;

import java.util.List;

public class ExpressionParser implements Parser {

    protected static final TokenPattern FIELD_PATTERN = TokenPattern.builder()
            .hollow()
            .unit(Separators.PERIOD)
            .hollow()
            .build();

    public Expression parse(ParserInfo info, TokenizedSource expressionSource) {
        if (expressionSource.size() == 1) {
            Token token = expressionSource.getToken(0);
            String value = token.getTokenValue();

            if (token.getType() == TokenType.LITERAL) {
                switch (token.getTokenValue()) {
                    case "null":
                        return new Expression(new PandaValue(null, null));
                    case "true":
                        return toSimpleKnownExpression("panda.lang:Boolean", true);
                    case "false":
                        return toSimpleKnownExpression("panda.lang:Boolean", false);
                    case "this":
                        ClassPrototype type = info.getComponent(Components.CLASS_PROTOTYPE);
                        return new Expression(type, new ThisExpressionCallback());
                    default:
                        throw new PandaParserException("Unknown literal: " + token);
                }
            }

            if (token.getType() == TokenType.SEQUENCE) {
                switch (token.getName()) {
                    case "String":
                        return toSimpleKnownExpression("panda.lang:String", value);
                    default:
                        throw new PandaParserException("Unknown sequence: " + token);
                }
            }

            if (NumberUtils.isNumber(value)) {
                return toSimpleKnownExpression("panda.lang:Int", Integer.parseInt(value));
            }

            ScopeLinker scopeLinker = info.getComponent(Components.SCOPE_LINKER);
            Scope scope = scopeLinker.getCurrentScope();
            Variable variable = VariableParserUtils.getVariable(scope, value);

            if (variable != null) {
                int memoryIndex = VariableParserUtils.indexOf(scope, variable);
                return new Expression(variable.getType(), new VariableExpressionCallback(memoryIndex));
            }

            ClassPrototype prototype = info.getComponent(Components.CLASS_PROTOTYPE);
            Field field = prototype.getField(value);

            if (field != null) {
                int memoryIndex = prototype.getFields().indexOf(field);
                return new Expression(field.getType(), new FieldExpressionCallback(ThisExpressionCallback.asExpression(prototype), field, memoryIndex));
            }
        }
        else if (TokenUtils.equals(expressionSource.get(0), TokenType.KEYWORD, "new")) {
            InstanceExpressionParser callbackParser = new InstanceExpressionParser();

            callbackParser.parse(expressionSource, info);
            InstanceExpressionCallback callback = callbackParser.toCallback();

            return new Expression(callback.getReturnType(), callback);
        }

        PandaTokenReader expressionReader = new PandaTokenReader(expressionSource);
        List<TokenizedSource> methodMatches = MethodInvokerExpressionParser.PATTERN.match(expressionReader);

        if (methodMatches != null && methodMatches.size() > 0) {
            MethodInvokerExpressionParser callbackParser = new MethodInvokerExpressionParser(methodMatches);

            callbackParser.parse(expressionSource, info);
            MethodInvokerExpressionCallback callback = callbackParser.toCallback();

            return new Expression(callback.getReturnType(), callback);
        }

        List<TokenizedSource> fieldMatches = FIELD_PATTERN.match(expressionReader);

        if (fieldMatches != null && fieldMatches.size() == 2) {
            Expression instanceExpression = parse(info, fieldMatches.get(0));
            ClassPrototype instanceType = instanceExpression.getReturnType();
            Field instanceField = instanceType.getField(fieldMatches.get(1).getLast().getToken().getTokenValue());

            if (instanceField == null) {

            }

            int memoryIndex = instanceType.getFields().indexOf(instanceField);
            return new Expression(instanceType, new FieldExpressionCallback(instanceExpression, instanceField, memoryIndex));
        }

        throw new PandaParserException("Cannot recognize expression: " + expressionSource.toString());
    }

    private Expression toSimpleKnownExpression(String forName, Object value) {
        return new Expression(new PandaValue(ClassPrototype.forName(forName), value));
    }

}
