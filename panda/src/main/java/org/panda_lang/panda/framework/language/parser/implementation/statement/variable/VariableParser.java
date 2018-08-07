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

package org.panda_lang.panda.framework.language.parser.implementation.statement.variable;

import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.language.parser.implementation.statement.variable.parser.*;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = VariableParser.class, handlerClass = VariableParserHandler.class, priority = PandaPriorities.STATEMENT_VARIABLE_PARSER)
public class VariableParser implements UnifiedParser {

    @Override
    public void parse(ParserData data) {
        CasualParserGeneration generation = data.getComponent(UniversalComponents.GENERATION);

        VarParser varParser = new VarParser();
        VarParserData parserData = varParser.toVarParserData(data, data.getComponent(UniversalComponents.SOURCE_STREAM));

        generation.getLayer(CasualParserGenerationType.HIGHER).delegateImmediately((delegatedData, nextLayer) -> {
            VarParserResult parserResult = varParser.parseVariable(parserData, delegatedData);

            if (parserResult.isFreshVariable()) {
                parserResult.getScope().addVariable(parserResult.getVariable());
            }

            if (!parserData.hasAssignation()) {
                return;
            }

            nextLayer.delegate((delegatedData1, nextLayer1) -> varParser.parseAssignation(parserData, parserResult, delegatedData1), delegatedData);
        }, data);

    }

    /*
    @LocalCallback
    private static class VariableDeclarationCallbackCasual implements CasualParserGenerationCallback {

        private final boolean assignation;

        private VariableDeclarationCallbackCasual(boolean assignation) {
            this.assignation = assignation;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor;

            if (assignation) {
                redactor = AbyssPatternAssistant.traditionalMapping(ASSIGNATION_PATTERN, delegatedData, "left", "right");
            }
            else {
                redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "left");
            }

            TokenizedSource left = redactor.get("left");

            Container container = delegatedData.getComponent(PandaComponents.CONTAINER);
            StatementCell cell = container.reserveCell();

            ScopeLinker linker = delegatedData.getComponent(PandaComponents.SCOPE_LINKER);
            Scope scope = linker.getCurrentScope();

            if (!parseLeft(delegatedData, left, scope)) {
                throw new PandaParserException("Cannot parse variable: " + left.asString());
            }

            if (assignation) {
                nextLayer.delegate(new VariableAssignationCasualParserCallback(redactor, scope, null, cell), delegatedData);
            }
        }

        private boolean parseLeft(ParserData delegatedInfo, TokenizedSource left, Scope scope) {
            if (left.size() > 2) {
                ExpressionParser expressionParser = new ExpressionParser();
                Expression instanceExpression = expressionParser.parse(delegatedInfo, left.subSource(0, left.size() - 2), true);

                if (instanceExpression != null) {
                    delegatedInfo.setComponent(VariableComponents.INSTANCE_EXPRESSION, instanceExpression);
                    delegatedInfo.setComponent(VariableComponents.INSTANCE_FIELD, left.getLast().getToken().getTokenValue());
                    return true;
                }
            }

            if (left.size() >= 2) {
                String variableName = left.getLast().getTokenValue();
                String variableType = left.getLast(1).getTokenValue();

                PandaScript script = delegatedInfo.getComponent(PandaComponents.PANDA_SCRIPT);
                ImportRegistry importRegistry = script.getImportRegistry();
                ClassPrototype type = importRegistry.forClass(variableType);
                boolean mutable = TokenUtils.contains(left, Keywords.MUTABLE);
                boolean nullable = TokenUtils.contains(left, Keywords.NULLABLE);

                if (type == null) {
                    throw new PandaParserException("Unknown type '" + variableType + "'");
                }

                Variable variable = new PandaVariable(type, variableName, 0, mutable, nullable);

                if (VariableParserUtils.checkDuplicates(scope, variable)) {
                    throw new PandaParserException("Variable '" + variableName + "' already exists in this scope");
                }

                scope.getVariables().add(variable);
                return true;
            }

            if (left.size() == 1) {
                Variable variable = VariableParserUtils.getVariable(scope, left.getLast().getToken().getTokenValue());

                if (variable != null) {
                    if (!variable.isMutable()) {
                        throw new PandaParserException("Cannot change value of immutable variable '" + variable.getInternalPath() + "' at line " + TokenUtils.getLine(left));
                    }
                }
                else {
                    ClassPrototype prototype = delegatedInfo.getComponent(PandaComponents.CLASS_PROTOTYPE);

                    if (prototype == null) {
                        throw new PandaParserException("Cannot get field from non-prototype scope");
                    }

                    delegatedInfo.setComponent("instance-expression", new PandaExpression(prototype, new ThisExpressionCallback()));
                    delegatedInfo.setComponent("instance-field", left.getLast().getToken().getTokenValue());
                }

                return true;
            }

            throw new PandaParserException("Unknown left side: " + left);
        }

    }

    @LocalCallback
    private static class VariableAssignationCasualParserCallback implements CasualParserGenerationCallback {

        private final AbyssRedactor redactor;
        private final Scope scope;
        private final Variable variable;
        private final StatementCell cell;

        private VariableAssignationCasualParserCallback(AbyssRedactor redactor, Scope scope, Variable variable, StatementCell cell) {
            this.redactor = redactor;
            this.scope = scope;
            this.variable = variable;
            this.cell = cell;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            TokenizedSource right = redactor.get("right");

            ExpressionParser expressionParser = new ExpressionParser();
            Expression expressionValue = expressionParser.parse(delegatedData, right);

            if (expressionValue == null) {
                throw new PandaParserException("Cannot parse expression '" + right + "'");
            }

            Statement accessor;

            if (variable != null) {
                if (!expressionValue.isNull() && !expressionValue.getReturnType().isAssociatedWith(variable.getType())) {
                    throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right)
                            + " (var: " + variable.getType().getClassName() + "; expr: " + expressionValue.getReturnType().getClassName() + ")");
                }

                accessor = new VariableAssigner(variable, VariableParserUtils.indexOf(scope, variable), expressionValue);
            }
            else {
                Expression instanceExpression = delegatedData.getComponent("instance-expression");
                String fieldName = delegatedData.getComponent("instance-field");

                ClassPrototype type = instanceExpression.getReturnType();
                PrototypeField field = type.getField(fieldName);

                if (field == null) {
                    throw new PandaParserException("Field '" + fieldName + "' does not belong to " + type.getClassName());
                }

                if (!field.getType().equals(expressionValue.getReturnType())) {
                    throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right));
                }

                accessor = new FieldAssigner(instanceExpression, field, expressionValue);
            }

            cell.setStatement(accessor);
        }

    }
    */

}
