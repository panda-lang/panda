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

package org.panda_lang.panda.interpreter.parser.bootstrap;

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.architecture.dynamic.block.main.MainScope;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.token.defaults.keyword.Keywords;
import org.panda_lang.panda.framework.language.interpreter.token.utils.TokenUtils;
import org.panda_lang.panda.framework.language.parser.bootstrap.InjectedLayer;
import org.panda_lang.panda.framework.language.parser.bootstrap.LocalData;
import org.panda_lang.panda.framework.language.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.parser.implementation.ScopeParser;

public class PandaParserBootstrapTest {

    @Test
    void testBootstrap() {
        ParserRepresentation representation = new PandaParserBootstrap()
                .pipeline(UniversalPipelines.OVERALL)
                .handler((reader) -> TokenUtils.equals(reader.read(), Keywords.MAIN))
                .pattern("main { +* }", "main-body")
                .parser()
                    .layer(new InjectedLayer() {
                        @Autowired
                        void parse(ParserData data, AbyssRedactor redactor, LocalData localData, @Component Script script) {
                            MainScope main = localData.createInstance(new MainScope());
                            script.getStatements().add(main);
                        }
                    })
                    .layer(new InjectedLayer() {
                        @Autowired
                        void parse(ParserData data, AbyssRedactor redactor, @Local MainScope main, @Redactor TokenizedSource body) {
                            ScopeParser.createParser(main, data)
                                    .forkData()
                                    .initializeLinker()
                                    .parse(body);
                        }
                    })
                    .end()
                .build();
    }

}
