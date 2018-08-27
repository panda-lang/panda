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

package org.panda_lang.panda.framework.language.parser.implementation.statement.scope.main;

import org.panda_lang.panda.framework.design.architecture.Script;
import org.panda_lang.panda.framework.design.architecture.dynamic.block.main.MainScope;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.ComponentQualifier;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.parser.implementation.ScopeParser;

@ParserRegistration(target = UniversalPipelines.OVERALL, parserClass = MainParser.class, handlerClass = MainParserHandler.class)
public class MainParser implements UnifiedParser {

    private final ParserRepresentation bootstrapParser = PandaParserBootstrap.builder()
            .pattern("main { +* }", "main-body")
            .instance(this)
            .build();

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        return bootstrapParser.getParser().parse(data, nextLayer);
    }

    @Autowired(order = 1)
    private void createScope(ParserData data, LocalData localData, @ComponentQualifier Script script) {
        MainScope main = localData.allocateInstance(new MainScope());
        script.getStatements().add(main);
    }

    @Autowired(value = Delegation.DEFAULT, order = 2)
    private void parseScope(ParserData data, @Local MainScope main, @Redactor("main-body") TokenizedSource body) {
        ScopeParser.createParser(main, data)
                .forkData()
                .initializeLinker()
                .parse(body);
    }

}
