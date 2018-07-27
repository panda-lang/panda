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

package org.panda_lang.panda.language.interpreter.parsers.statement.scope.main;

import org.panda_lang.panda.design.architecture.dynamic.block.main.*;
import org.panda_lang.panda.design.interpreter.parser.*;
import org.panda_lang.panda.design.interpreter.parser.defaults.*;
import org.panda_lang.panda.design.interpreter.parser.generation.*;
import org.panda_lang.panda.design.interpreter.parser.linker.*;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.architecture.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;
import org.panda_lang.panda.language.interpreter.*;

@ParserRegistration(target = UniversalPipelines.OVERALL, parserClass = MainParser.class, handlerClass = MainParserHandler.class)
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
