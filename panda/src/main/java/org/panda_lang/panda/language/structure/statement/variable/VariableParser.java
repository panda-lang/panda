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

package org.panda_lang.panda.language.structure.statement.variable;

import org.panda_lang.panda.design.architecture.PandaScript;
import org.panda_lang.panda.framework.language.architecture.value.PandaVariable;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.design.runtime.expression.PandaExpression;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.architecture.statement.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.general.expression.callbacks.instance.ThisExpressionCallback;
import org.panda_lang.panda.framework.design.architecture.module.ImportRegistry;
import org.panda_lang.panda.language.structure.statement.invoker.MethodInvokerParser;
import org.panda_lang.panda.design.architecture.statement.assigner.FieldAssigner;
import org.panda_lang.panda.design.architecture.statement.assigner.VariableAssigner;
import org.panda_lang.panda.language.syntax.PandaSyntax;
import org.panda_lang.panda.language.syntax.tokens.Keywords;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.STATEMENT, parserClass = VariableParser.class, handlerClass = VariableParserHandler.class, priority = DefaultPriorities.STATEMENT_VARIABLE_PARSER)
public class VariableParser implements UnifiedParser {

    public static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+*")
            .build();

    public static final AbyssPattern ASSIGNATION_PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** = +*")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(PandaComponents.GENERATION);
        CasualParserGenerationCallback callback;

        Extractor extractor = VariableParser.ASSIGNATION_PATTERN.extractor();
        SourceStream stream = info.getComponent(PandaComponents.SOURCE_STREAM);

        SourceStream copyOfStream = new PandaSourceStream(stream.toTokenizedSource());
        List<TokenizedSource> hollows = extractor.extract(copyOfStream.toTokenReader());

        if (hollows == null || hollows.size() != 2) {
            // TODO
            Extractor invokerExtractor = MethodInvokerParser.PATTERN.extractor();
            SourceStream invokerStream = new PandaSourceStream(stream.toTokenizedSource());
            List<TokenizedSource> invokerHollows = extractor.extract(copyOfStream.toTokenReader());

            callback = new VariableDeclarationCallbackCasual(false);
        }
        else {
            callback = new VariableDeclarationCallbackCasual(true);
        }

        generation.getLayer(CasualParserGenerationType.HIGHER).delegateImmediately(callback, info.fork());
    }

    @LocalCallback
    private static class VariableDeclarationCallbackCasual implements CasualParserGenerationCallback {

        private final boolean assignation;

        private VariableDeclarationCallbackCasual(boolean assignation) {
            this.assignation = assignation;
        }

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor;

            if (assignation) {
                redactor = AbyssPatternAssistant.traditionalMapping(ASSIGNATION_PATTERN, delegatedInfo, "left", "right");
            }
            else {
                redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedInfo, "left");
            }

            delegatedInfo.setComponent("redactor", redactor);
            TokenizedSource left = redactor.get("left");

            Container container = delegatedInfo.getComponent("container");
            StatementCell cell = container.reserveCell();
            delegatedInfo.setComponent("cell", cell);

            ScopeLinker linker = delegatedInfo.getComponent(PandaComponents.SCOPE_LINKER);
            Scope scope = linker.getCurrentScope();
            delegatedInfo.setComponent("scope", scope);

            if (!parseLeft(delegatedInfo, left, scope)) {
                throw new PandaParserException("Cannot parse variable: " + left.asString());
            }

            if (assignation) {
                nextLayer.delegate(new VariableAssignationCasualParserCallback(), delegatedInfo);
            }
        }

        private boolean parseLeft(ParserInfo delegatedInfo, TokenizedSource left, Scope scope) {
            if (left.size() > 2) {
                ExpressionParser expressionParser = new ExpressionParser();
                Expression instanceExpression = expressionParser.parse(delegatedInfo, left.subSource(0, left.size() - 2), true);

                if (instanceExpression != null) {
                    delegatedInfo.setComponent("instance-expression", instanceExpression);
                    delegatedInfo.setComponent("instance-field", left.getLast().getToken().getTokenValue());
                    return true;
                }
            }

            if (left.size() >= 2) {
                String variableName = left.getLast().getTokenValue();
                String variableType = left.getLast(1).getTokenValue();

                PandaScript script = delegatedInfo.getComponent(PandaComponents.SCRIPT);
                ImportRegistry importRegistry = script.getImportRegistry();
                ClassPrototype type = importRegistry.forClass(variableType);
                boolean mutable = TokenUtils.contains(left, Keywords.MUTABLE);
                boolean nullable = TokenUtils.contains(left, Keywords.NULLABLE);

                if (type == null) {
                    throw new PandaParserException("Unknown type '" + variableType + "'");
                }

                Variable variable = new PandaVariable(type, variableName, 0, mutable, nullable);
                delegatedInfo.setComponent("variable", variable);

                if (VariableParserUtils.checkDuplicates(scope, variable)) {
                    throw new PandaParserException("Variable '" + variableName + "' already exists in this scope");
                }

                scope.getVariables().add(variable);
                return true;
            }

            if (left.size() == 1) {
                Variable variable = VariableParserUtils.getVariable(scope, left.getLast().getToken().getTokenValue());

                if (variable != null) {
                    delegatedInfo.setComponent("variable", variable);

                    if (!variable.isMutable()) {
                        throw new PandaParserException("Cannot change value of immutable variable '" + variable.getName() + "' at line " + TokenUtils.getLine(left));
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

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource right = redactor.get("right");

            ExpressionParser expressionParser = new ExpressionParser();
            Expression expressionValue = expressionParser.parse(delegatedInfo, right);

            if (expressionValue == null) {
                throw new PandaParserException("Cannot parse expression '" + right + "'");
            }

            Scope scope = delegatedInfo.getComponent("scope");
            Variable variable = delegatedInfo.getComponent("variable");
            Statement assigner;

            if (variable != null) {
                if (!expressionValue.isNull() && !expressionValue.getReturnType().isAssociatedWith(variable.getType())) {
                    throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right)
                            + " (var: " + variable.getType().getClassName() + "; expr: " + expressionValue.getReturnType().getClassName() + ")");
                }

                assigner = new VariableAssigner(variable, VariableParserUtils.indexOf(scope, variable), expressionValue);
            }
            else {
                Expression instanceExpression = delegatedInfo.getComponent("instance-expression");
                String fieldName = delegatedInfo.getComponent("instance-field");

                ClassPrototype type = instanceExpression.getReturnType();
                PrototypeField field = type.getField(fieldName);

                if (field == null) {
                    throw new PandaParserException("Field '" + fieldName + "' does not belong to " + type.getClassName());
                }

                if (!field.getType().equals(expressionValue.getReturnType())) {
                    throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right));
                }

                assigner = new FieldAssigner(instanceExpression, field, expressionValue);
            }

            StatementCell cell = delegatedInfo.getComponent("cell");
            cell.setStatement(assigner);
        }

    }

}
