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

package org.panda_lang.panda.framework.language.interpreter.parsers.statement;

import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.*;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.*;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.*;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.*;
import org.panda_lang.panda.framework.language.interpreter.token.utils.*;
import org.panda_lang.panda.framework.language.interpreter.*;
import org.panda_lang.panda.framework.language.interpreter.parsers.*;

@ParserRegistration(target = PandaPipelines.SCOPE, parserClass = StatementParser.class, handlerClass = StatementParserHandler.class, priority = PandaPriorities.STATEMENT_VARIABLE_PARSER)
public class StatementParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+* ;")
            .build();

    @Override
    public void parse(ParserData data) {
        PipelineRegistry pipelineRegistry = data.getComponent(UniversalComponents.PIPELINE);
        ParserPipeline pipeline = pipelineRegistry.getPipeline(PandaPipelines.STATEMENT);

        AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, data);
        TokenizedSource source = hollows.getGap(0);

        SourceStream declarationStream = new PandaSourceStream(source);
        UnifiedParser statementParser = pipeline.handle(declarationStream);

        if (statementParser == null) {
            throw new PandaParserException("Cannot recognize block at line " + TokenUtils.getLine(source));
        }

        ParserData statementParserData = data.fork();
        statementParserData.setComponent(UniversalComponents.SOURCE_STREAM, declarationStream);
        statementParser.parse(statementParserData);
    }

    @LocalCallback
    private static class DeclarationParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            PipelineRegistry pipelineRegistry = delegatedData.getComponent(UniversalComponents.PIPELINE);
            ParserPipeline pipeline = pipelineRegistry.getPipeline(PandaPipelines.STATEMENT);

            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, delegatedData);
            TokenizedSource source = hollows.getGap(0);

            SourceStream declarationStream = new PandaSourceStream(source);
            UnifiedParser statementParser = pipeline.handle(declarationStream);

            if (statementParser == null) {
                throw new PandaParserException("Cannot recognize statement at line " + TokenUtils.getLine(source));
            }

            ParserData statementParserData = delegatedData.fork();
            statementParserData.setComponent(UniversalComponents.SOURCE_STREAM, declarationStream);
            statementParser.parse(statementParserData);
        }

    }

}
