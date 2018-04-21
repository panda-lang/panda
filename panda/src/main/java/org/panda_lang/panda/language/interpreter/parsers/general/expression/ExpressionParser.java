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

package org.panda_lang.panda.language.interpreter.parsers.general.expression;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.framework.design.architecture.Environment;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.ParticularParser;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.reader.TokenReader;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.reader.PandaTokenReader;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.instance.InstanceExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.instance.InstanceExpressionParser;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.invoker.MethodInvokerExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.invoker.MethodInvokerExpressionParser;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.invoker.MethodInvokerExpressionUtils;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.logic.NotLogicalExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.math.MathExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.math.MathExpressionUtils;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.math.MathParser;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.memory.FieldExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.memory.VariableExpressionCallback;
import org.panda_lang.panda.language.interpreter.parsers.general.number.NumberExpressionParser;
import org.panda_lang.panda.language.interpreter.parsers.general.number.NumberUtils;
import org.panda_lang.panda.language.interpreter.parsers.statement.variable.VariableParserUtils;
import org.panda_lang.panda.language.interpreter.tokens.Operators;

import java.util.List;

public class ExpressionParser implements ParticularParser<Expression> {

    @Override
    public Expression parse(ParserData data, TokenizedSource expressionSource) {
        return parse(data, expressionSource, false);
    }

    public Expression parse(ParserData info, TokenizedSource expressionSource, boolean silence) {
        Environment environment = info.getComponent(PandaComponents.ENVIRONMENT);
        ModuleRegistry registry = environment.getModuleRegistry();

        if (expressionSource.size() == 1) {
            Token token = expressionSource.getToken(0);
            String value = token.getTokenValue();

            if (token.getType() == TokenType.LITERAL) {
                switch (token.getTokenValue()) {
                    case "null":
                        return new PandaExpression(new PandaValue(null, null));
                    case "true":
                        return toSimpleKnownExpression(registry, "boolean", true);
                    case "false":
                        return toSimpleKnownExpression(registry, "boolean", false);
                    case "this":
                        ClassPrototype type = info.getComponent(PandaComponents.CLASS_PROTOTYPE);
                        return new PandaExpression(type, new ThisExpressionCallback());
                    default:
                        throw new PandaParserException("Unknown literal: " + token);
                }
            }

            if (token.getType() == TokenType.SEQUENCE) {
                switch (token.getName()) {
                    case "String":
                        return toSimpleKnownExpression(registry, "java.lang:String", value);
                    default:
                        throw new PandaParserException("Unknown sequence: " + token);
                }
            }

            NumberExpressionParser numberExpressionParser = new NumberExpressionParser();
            Value numericValue = numberExpressionParser.parse(info, expressionSource);

            if (numericValue != null) {
                return new PandaExpression(numericValue);
            }

            ScopeLinker scopeLinker = info.getComponent(PandaComponents.SCOPE_LINKER);
            Scope scope = scopeLinker.getCurrentScope();
            Variable variable = VariableParserUtils.getVariable(scope, value);

            if (variable != null) {
                int memoryIndex = VariableParserUtils.indexOf(scope, variable);
                return new PandaExpression(variable.getType(), new VariableExpressionCallback(memoryIndex));
            }

            ClassPrototype prototype = info.getComponent(PandaComponents.CLASS_PROTOTYPE);

            if (prototype != null) {
                PrototypeField field = prototype.getField(value);

                if (field != null) {
                    int memoryIndex = prototype.getFields().indexOf(field);
                    return new PandaExpression(field.getType(), new FieldExpressionCallback(ThisExpressionCallback.asExpression(prototype), field, memoryIndex));
                }
            }
        }

        if (TokenUtils.equals(expressionSource.getFirst(), Operators.NOT)) {
            Expression expression = parse(info, expressionSource.subSource(1, expressionSource.size()));
            return new PandaExpression(expression.getReturnType(), new NotLogicalExpressionCallback(expression));
        }

        MethodInvokerExpressionParser methodInvokerParser = MethodInvokerExpressionUtils.match(expressionSource);

        if (methodInvokerParser != null) {
            methodInvokerParser.parse(expressionSource, info);
            MethodInvokerExpressionCallback callback = methodInvokerParser.toCallback();

            return new PandaExpression(callback.getReturnType(), callback);
        }

        TokenReader expressionReader = new PandaTokenReader(expressionSource);
        List<TokenizedSource> constructorMatches = ExpressionPatterns.INSTANCE_PATTERN.match(expressionReader);

        if (constructorMatches != null && constructorMatches.size() == 3 && constructorMatches.get(2).size() == 0) {
            InstanceExpressionParser callbackParser = new InstanceExpressionParser();

            callbackParser.parse(expressionSource, info);
            InstanceExpressionCallback callback = callbackParser.toCallback();

            return new PandaExpression(callback.getReturnType(), callback);
        }

        List<TokenizedSource> fieldMatches = ExpressionPatterns.FIELD_PATTERN.match(expressionReader);

        if (fieldMatches != null && fieldMatches.size() == 2 && !NumberUtils.startsWithNumber(fieldMatches.get(1))) {
            PandaScript script = info.getComponent(PandaComponents.SCRIPT);
            ImportRegistry importRegistry = script.getImportRegistry();

            TokenizedSource instanceSource = fieldMatches.get(0);
            ClassPrototype instanceType = null;
            Expression fieldLocationExpression = null;

            if (instanceSource.size() == 1) {
                instanceType = importRegistry.forClass(fieldMatches.get(0).asString());
            }

            if (instanceType == null) {
                fieldLocationExpression = parse(info, fieldMatches.get(0));
                instanceType = fieldLocationExpression.getReturnType();
            }

            if (instanceType == null) {
                throw new PandaParserException("Unknown instance source at line " + TokenUtils.getLine(instanceSource));
            }

            String instanceFieldName = fieldMatches.get(1).asString();
            PrototypeField instanceField = instanceType.getField(instanceFieldName);

            if (instanceField == null) {
                throw new PandaParserException("Class " + instanceType.getClassName() + " does not contain field " + instanceFieldName + " at " + TokenUtils.getLine(expressionSource));
            }

            int memoryIndex = instanceType.getFields().indexOf(instanceField);
            return new PandaExpression(instanceField.getType(), new FieldExpressionCallback(fieldLocationExpression, instanceField, memoryIndex));
        }

        NumberExpressionParser numberExpressionParser = new NumberExpressionParser();
        Value numericValue = numberExpressionParser.parse(info, expressionSource);

        if (numericValue != null) {
            return new PandaExpression(numericValue);
        }

        if (MathExpressionUtils.isMathExpression(expressionSource)) {
            MathParser mathParser = new MathParser();
            MathExpressionCallback expression = mathParser.parse(expressionSource, info);
            return new PandaExpression(expression.getReturnType(), expression);
        }

        if (!silence) {
            throw new PandaParserException("Cannot recognize expression: " + expressionSource.toString());
        }

        return null;
    }

    public Expression toSimpleKnownExpression(ModuleRegistry registry, String forName, Object value) {
        return new PandaExpression(new PandaValue(registry.forName(forName), value));
    }

}
