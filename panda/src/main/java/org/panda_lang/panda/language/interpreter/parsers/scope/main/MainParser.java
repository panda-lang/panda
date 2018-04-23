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

package org.panda_lang.panda.language.interpreter.parsers.scope.main;

import org.panda_lang.panda.design.architecture.dynamic.main.MainScope;
import org.panda_lang.panda.design.interpreter.parser.defaults.ScopeParser;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.design.interpreter.parser.linker.PandaScopeLinker;
import org.panda_lang.panda.design.interpreter.parser.linker.ScopeLinker;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

@ParserRegistration(target = PandaPipelines.OVERALL, parserClass = MainParser.class, handlerClass = MainParserHandler.class)
public class MainParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "main { +* }")
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new MainDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class MainDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "main-body");

            MainScope main = new MainScope();
            Script script = delegatedData.getComponent(UniversalComponents.SCRIPT);
            script.getStatements().add(main);

            nextLayer.delegate(new MainBodyCasualParserCallback(main, redactor), delegatedData.fork());
        }

    }

    @LocalCallback
    private static class MainBodyCasualParserCallback implements CasualParserGenerationCallback {

        private final MainScope main;
        private final AbyssRedactor redactor;

        public MainBodyCasualParserCallback(MainScope main, AbyssRedactor redactor) {
            this.main = main;
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            ScopeLinker linker = new PandaScopeLinker(main);
            delegatedData.setComponent(PandaComponents.SCOPE_LINKER, linker);

            TokenizedSource body = redactor.get("main-body");
            ScopeParser scopeParser = new ScopeParser(main);
            scopeParser.parse(delegatedData, body);
        }

    }

}
