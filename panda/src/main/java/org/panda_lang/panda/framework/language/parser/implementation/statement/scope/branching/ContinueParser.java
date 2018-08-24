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

package org.panda_lang.panda.framework.language.parser.implementation.statement.scope.branching;

import org.panda_lang.panda.framework.design.architecture.dynamic.branching.Continue;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.architecture.statement.PandaStatementData;
import org.panda_lang.panda.framework.language.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Component;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = ContinueParser.class, handlerClass = ContinueParserHandler.class)
public class ContinueParser implements UnifiedParser {

    private ParserRepresentation bootstrapParser = PandaParserBootstrap.builder()
            .instance(this)
            .build();

    @Override
    public boolean parse(ParserData data, CasualParserGenerationLayer nextLayer) {
        return bootstrapParser.getParser().parse(data, nextLayer);
    }

    @Autowired
    private void parseContinue(@Component SourceStream source, @Component Container container) {
        Continue continueStatement = new Continue();
        container.addStatement(continueStatement);
        continueStatement.setStatementData(PandaStatementData.of(source));
    }

}
