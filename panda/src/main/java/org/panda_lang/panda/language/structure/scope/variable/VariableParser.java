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

package org.panda_lang.panda.language.structure.scope.variable;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.core.structure.value.PandaVariable;
import org.panda_lang.panda.core.structure.value.Variable;
import org.panda_lang.panda.core.structure.wrapper.Container;
import org.panda_lang.panda.core.structure.wrapper.Scope;
import org.panda_lang.panda.core.structure.wrapper.StatementCell;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.extractor.Extractor;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

import java.util.List;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = VariableParser.class, handlerClass = VariableParserHandler.class, priority = DefaultPriorities.SCOPE_VARIABLE_PARSER)
public class VariableParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .simpleHollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    protected static final TokenPattern SPECIFIED_PATTERN = TokenPattern.builder()
            .simpleHollow()
            .unit(TokenType.OPERATOR, "=")
            .hollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);
        CasualParserGenerationCallback callback;

        Extractor extractor = VariableParser.SPECIFIED_PATTERN.extractor();
        SourceStream stream = info.getComponent(Components.SOURCE_STREAM);
        SourceStream copyOfStream = new PandaSourceStream(stream.toTokenizedSource());
        List<TokenizedSource> hollows = extractor.extract(copyOfStream.toTokenReader());

        if (hollows == null || hollows.size() < 2) {
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
            TokenPatternHollows hollows = TokenPatternUtils.extract(assignation ? SPECIFIED_PATTERN : PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            if (assignation) {
                redactor.map("left", "right");
            }
            else {
                redactor.map("left");
            }

            delegatedInfo.setComponent("redactor", redactor);
            TokenizedSource left = redactor.get("left");

            Container container = delegatedInfo.getComponent("container");
            StatementCell cell = container.reserveCell();
            delegatedInfo.setComponent("cell", cell);

            if (left.size() == 2) {
                String variableType = left.getToken(0).getTokenValue();
                String variableName = left.getToken(1).getTokenValue();

                PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
                ImportRegistry importRegistry = script.getImportRegistry();
                ClassPrototype type = importRegistry.forClass(variableType);

                if (type == null) {
                    throw new PandaParserException("Unknown type '" + variableType + "'");
                }

                ScopeLinker linker = delegatedInfo.getComponent(Components.SCOPE_LINKER);
                Scope scope = linker.getCurrentScope();
                delegatedInfo.setComponent("scope", scope);

                Variable variable = new PandaVariable(type, variableName, 0);
                delegatedInfo.setComponent("variable", variable);

                if (VariableParserUtils.checkDuplicates(scope, variable)) {
                    throw new PandaParserException("Variable '" + variableName + "' already exists in this scope");
                }

                scope.getVariables().add(variable);
            }
            else if (left.size() == 1) {
                String variableName = left.getToken(0).getTokenValue();

                ScopeLinker linker = delegatedInfo.getComponent(Components.SCOPE_LINKER);
                Scope scope = linker.getCurrentScope();
                delegatedInfo.setComponent("scope", scope);

                Variable variable = VariableParserUtils.getVariable(scope, variableName);

                if (variable == null) {
                    ClassPrototype prototype = delegatedInfo.getComponent(Components.CLASS_PROTOTYPE);

                    if (prototype != null) {
                        variable = prototype.getField(variableName);
                        delegatedInfo.setComponent("f_variable", true);
                    }
                }

                if (variable == null) {
                    throw new PandaParserException("Variable " + variableName + " + is not defined");
                }

                delegatedInfo.setComponent("variable", variable);
            }
            else if (left.size() > 2) {
                // TODO: impl
                throw new PandaParserException("Not implemented");
            }
            else {
                throw new PandaParserException("Unknown left side: " + left);
            }

            if (assignation) {
                nextLayer.delegate(new VariableAssignationCasualParserCallback(), delegatedInfo);
            }
        }

    }

    @LocalCallback
    private static class VariableAssignationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource right = redactor.get("right");

            ExpressionParser expressionParser = new ExpressionParser();
            Expression expression = expressionParser.parse(delegatedInfo, right);

            if (expression == null) {
                throw new PandaParserException("Cannot parse expression '" + right + "'");
            }

            Scope scope = delegatedInfo.getComponent("scope");
            Variable variable = delegatedInfo.getComponent("variable");

            if (!variable.getType().equals(expression.getReturnType())) {
                throw new PandaParserException("Return type is incompatible with the type of variable at line " + TokenUtils.getLine(right));
            }

            Boolean variableType = delegatedInfo.getComponent("f_variable");
            Executable assigner;

            if (variableType != null && variableType) {
                ClassPrototype prototype = delegatedInfo.getComponent(Components.CLASS_PROTOTYPE);
                assigner = new FieldAssigner(prototype.getFields().indexOf(variable), expression);
            }
            else {
                assigner = new VariableAssigner(VariableParserUtils.indexOf(scope, variable), expression);
            }

            StatementCell cell = delegatedInfo.getComponent("cell");
            cell.setStatement(assigner);
        }

    }

}
