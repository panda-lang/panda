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

package org.panda_lang.panda.language.structure.overall.main;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.core.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.Script;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = MainParser.class, handlerClass = MainParserHandler.class)
public class MainParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "main")
            .unit(TokenType.SEPARATOR, "{")
            .hollow()
            .unit(TokenType.SEPARATOR, "}")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(CasualParserGenerationType.HIGHER)
                .delegateImmediately(new MainDeclarationCasualParserCallback(), info);
    }

    @LocalCallback
    private static class MainDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            Main main = new Main();
            delegatedInfo.setComponent("main", main);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);
            delegatedInfo.setComponent("redactor", redactor);

            redactor.map("main-body");
            nextLayer.delegate(new MainBodyCasualParserCallback(), delegatedInfo.fork());

            Script script = delegatedInfo.getComponent(Components.SCRIPT);
            script.getStatements().add(main);
        }

    }

    @LocalCallback
    private static class MainBodyCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            Main main = delegatedInfo.getComponent("main");
            delegatedInfo.setComponent("scope", main);

            ScopeLinker linker = new PandaScopeLinker(main);
            delegatedInfo.setComponent(Components.SCOPE_LINKER, linker);

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("main-body");

            ScopeParser scopeParser = new ScopeParser(main);
            scopeParser.parse(delegatedInfo, body);
        }

    }

}
