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

package org.panda_lang.panda.language.structure.main;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenType;
import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.panda.framework.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenHollowRedactor;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPattern;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternHollows;
import org.panda_lang.panda.implementation.interpreter.lexer.token.pattern.TokenPatternUtils;
import org.panda_lang.panda.implementation.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.implementation.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.implementation.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.implementation.structure.Script;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = MainParser.class, handlerClass = MainParserHandler.class)
public class MainParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "main")
            .unit(TokenType.SEPARATOR, "{")
            .hollow()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public void parse(ParserInfo parserInfo) {
        ParserGeneration generation = parserInfo.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new MainDeclarationParserCallback(), parserInfo);
    }

    @LocalCallback
    private static class MainDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            Main main = new Main();

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            delegatedInfo.setComponent("main", main);
            delegatedInfo.setComponent("redactor", redactor);

            redactor.map("body");
            nextLayer.delegate(new MainBodyParserCallback(), delegatedInfo.fork());

            Script script = delegatedInfo.getComponent(Components.SCRIPT);
            script.getStatements().add(main);
        }

    }

    @LocalCallback
    private static class MainBodyParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            Main main = delegatedInfo.getComponent("main");
            delegatedInfo.setComponent("scope", main);

            ScopeLinker linker = new PandaScopeLinker(main);
            delegatedInfo.setComponent(Components.LINKER, linker);

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("body");

            ScopeParser scopeParser = new ScopeParser(main);
            scopeParser.parse(delegatedInfo, body);
        }

    }

}
