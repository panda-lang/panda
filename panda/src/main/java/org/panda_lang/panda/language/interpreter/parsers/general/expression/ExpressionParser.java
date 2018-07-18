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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.design.architecture.*;
import org.panda_lang.panda.design.architecture.module.*;
import org.panda_lang.panda.design.interpreter.parser.*;
import org.panda_lang.panda.design.interpreter.parser.linker.*;
import org.panda_lang.panda.framework.design.architecture.module.*;
import org.panda_lang.panda.framework.design.architecture.prototype.*;
import org.panda_lang.panda.framework.design.architecture.prototype.field.*;
import org.panda_lang.panda.framework.design.architecture.statement.*;
import org.panda_lang.panda.framework.design.architecture.value.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.operator.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.design.runtime.expression.*;
import org.panda_lang.panda.framework.language.architecture.value.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.interpreter.token.utils.*;
import org.panda_lang.panda.framework.language.runtime.expression.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.instance.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.invoker.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.logic.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.math.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.callbacks.memory.*;
import org.panda_lang.panda.language.interpreter.parsers.general.number.*;
import org.panda_lang.panda.language.interpreter.parsers.prototype.parsers.*;
import org.panda_lang.panda.language.interpreter.parsers.statement.variable.*;

import java.util.*;

public class ExpressionParser implements ParticularParser<Expression> {

    @Override
    public Expression parse(ParserData data, TokenizedSource expressionSource) {
        return parse(data, expressionSource, false);
    }

    public @Nullable Expression parse(ParserData data, TokenizedSource expressionSource, boolean silence) {
        ModuleRegistry registry = data.getComponent(PandaComponents.MODULE_REGISTRY);

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
                        ClassPrototype type = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);
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
            Value numericValue = numberExpressionParser.parse(data, expressionSource);

            if (numericValue != null) {
                return new PandaExpression(numericValue);
            }

            ScopeLinker scopeLinker = data.getComponent(PandaComponents.SCOPE_LINKER);
            Scope scope = scopeLinker.getCurrentScope();
            Variable variable = VariableParserUtils.getVariable(scope, value);

            if (variable != null) {
                int memoryIndex = VariableParserUtils.indexOf(scope, variable);
                return new PandaExpression(variable.getType(), new VariableExpressionCallback(memoryIndex));
            }

            ClassPrototype prototype = data.getComponent(ClassPrototypeComponents.CLASS_PROTOTYPE);

            if (prototype != null) {
                PrototypeField field = prototype.getField(value);

                if (field != null) {
                    int memoryIndex = prototype.getFields().indexOf(field);
                    return new PandaExpression(field.getType(), new FieldExpressionCallback(ThisExpressionCallback.asExpression(prototype), field, memoryIndex));
                }
            }
        }

        if (TokenUtils.equals(expressionSource.getFirst(), Operators.NOT)) {
            Expression expression = parse(data, expressionSource.subSource(1, expressionSource.size()));
            return new PandaExpression(expression.getReturnType(), new NotLogicalExpressionCallback(expression));
        }

        MethodInvokerExpressionParser methodInvokerParser = MethodInvokerExpressionUtils.match(expressionSource);

        if (methodInvokerParser != null) {
            methodInvokerParser.parse(expressionSource, data);
            MethodInvokerExpressionCallback callback = methodInvokerParser.toCallback();

            return new PandaExpression(callback.getReturnType(), callback);
        }

        TokenReader expressionReader = new PandaTokenReader(expressionSource);
        List<TokenizedSource> constructorMatches = ExpressionPatterns.INSTANCE_PATTERN.match(expressionReader);

        if (constructorMatches != null && constructorMatches.size() == 3 && constructorMatches.get(2).size() == 0) {
            InstanceExpressionParser callbackParser = new InstanceExpressionParser();

            callbackParser.parse(expressionSource, data);
            InstanceExpressionCallback callback = callbackParser.toCallback();

            return new PandaExpression(callback.getReturnType(), callback);
        }

        List<TokenizedSource> fieldMatches = ExpressionPatterns.FIELD_PATTERN.match(expressionReader);

        if (fieldMatches != null && fieldMatches.size() == 2 && !NumberUtils.startsWithNumber(fieldMatches.get(1))) {
            PandaScript script = data.getComponent(PandaComponents.PANDA_SCRIPT);
            ImportRegistry importRegistry = script.getImportRegistry();

            TokenizedSource instanceSource = fieldMatches.get(0);
            ClassPrototype instanceType = null;
            Expression fieldLocationExpression = null;

            if (instanceSource.size() == 1) {
                instanceType = importRegistry.forClass(fieldMatches.get(0).asString());
            }

            if (instanceType == null) {
                fieldLocationExpression = parse(data, fieldMatches.get(0));
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
        Value numericValue = numberExpressionParser.parse(data, expressionSource);

        if (numericValue != null) {
            return new PandaExpression(numericValue);
        }

        if (MathExpressionUtils.isMathExpression(expressionSource)) {
            MathParser mathParser = new MathParser();
            MathExpressionCallback expression = mathParser.parse(expressionSource, data);
            return new PandaExpression(expression.getReturnType(), expression);
        }

        if (!silence) {
            throw new PandaParserException("Cannot recognize expression: " + expressionSource.toString());
        }

        return null;
    }

    public Expression toSimpleKnownExpression(ModuleRegistry registry, String forName, Object value) {
        return new PandaExpression(new PandaValue(PandaModuleRegistryAssistant.forName(registry, forName), value));
    }

}
