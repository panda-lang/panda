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

import org.panda_lang.panda.framework.design.architecture.dynamic.branching.Break;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.architecture.statement.PandaStatementData;
import org.panda_lang.panda.framework.language.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.ComponentQualifier;

@ParserRegistration(target = PandaPipelines.STATEMENT, parserClass = BreakParser.class, handlerClass = BreakParserHandler.class)
public class BreakParser implements UnifiedParser {

    private ParserRepresentation bootstrapParser = PandaParserBootstrap.builder()
            .instance(this)
            .build();

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        return bootstrapParser.getParser().parse(data, nextLayer);
    }

    @Autowired
    private void parseBreak(ParserData data, @ComponentQualifier SourceStream source, @ComponentQualifier Container container) {
        Break breakStatement = new Break();
        container.addStatement(breakStatement);
        breakStatement.setStatementData(PandaStatementData.of(source));
    }

}
