/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.structure.variable;

import org.panda_lang.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.framework.interpreter.parser.ParserInfo;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.implementation.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.implementation.structure.PandaScript;
import org.panda_lang.panda.implementation.structure.value.PandaVariable;
import org.panda_lang.panda.implementation.structure.value.Variable;
import org.panda_lang.panda.implementation.structure.wrapper.Scope;
import org.panda_lang.panda.language.structure.expression.Expression;
import org.panda_lang.panda.language.structure.expression.ExpressionParser;
import org.panda_lang.panda.language.structure.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.ClassPrototype;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = VariableParser.class, handlerClass = VariableParserHandler.class, priority = DefaultPriorities.VARIABLE_PARSER)
public class VariableParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .hollow()
            .unit(TokenType.OPERATOR, "=")
            .hollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new VariableDeclarationCallback(), parserInfo.fork());
    }

    @LocalCallback
    private static class VariableDeclarationCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("left", "right");
            delegatedInfo.setComponent("redactor", redactor);

            TokenizedSource left = redactor.get("left");

            if (left.size() == 2) {
                String variableType = left.getToken(0).getTokenValue();
                String variableName = left.getToken(1).getTokenValue();

                PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
                ImportRegistry importRegistry = script.getImportRegistry();
                ClassPrototype type = importRegistry.forClass(variableType);

                if (type == null) {
                    throw new PandaParserException("Unknown type '" + variableType + "'");
                }

                ScopeLinker linker = delegatedInfo.getComponent(Components.LINKER);
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

                ScopeLinker linker = delegatedInfo.getComponent(Components.LINKER);
                Scope scope = linker.getCurrentScope();
                delegatedInfo.setComponent("scope", scope);

                Variable variable = VariableParserUtils.getVariable(scope, variableName);
                delegatedInfo.setComponent("variable", variable);
            }
            else {
                throw new PandaParserException("Unknown left side: " + left);
            }

            nextLayer.delegate(new VariableParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class VariableParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource right = redactor.get("right");

            ExpressionParser expressionParser = new ExpressionParser();
            Expression expression = expressionParser.parse(delegatedInfo, right);

            if (expression == null) {
                throw new PandaParserException("Cannot parse expression '" + right + "'");
            }

            Scope scope = delegatedInfo.getComponent("scope");
            Variable variable = delegatedInfo.getComponent("variable");
            Assigner assigner = new Assigner(VariableParserUtils.indexOf(scope, variable), expression);

            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
            script.addStatement(assigner);
        }

    }

}
