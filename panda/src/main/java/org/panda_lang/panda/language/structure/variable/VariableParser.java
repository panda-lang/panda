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
import org.panda_lang.framework.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.framework.interpreter.parser.util.Components;
import org.panda_lang.framework.structure.Scope;
import org.panda_lang.framework.structure.Variable;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.implementation.structure.PandaVariable;

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
            String variableType = left.getToken(0).getTokenValue();
            String variableName = left.getToken(1).getTokenValue();

            Variable variable = new PandaVariable(variableType, variableName);
            System.out.println(variable);

            ScopeLinker linker = delegatedInfo.getComponent(Components.LINKER);
            Scope scope = linker.getCurrentScope();

            scope.getVariables().add(variable);
            nextLayer.delegate(new VariableParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class VariableParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            System.out.println("hello");
        }

    }

}
