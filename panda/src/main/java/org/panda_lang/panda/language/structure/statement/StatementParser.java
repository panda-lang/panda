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

package org.panda_lang.panda.language.structure.statement;

import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.design.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactorHollows;
import org.panda_lang.panda.language.syntax.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = StatementParser.class, handlerClass = StatementParserHandler.class, priority = DefaultPriorities.STATEMENT_VARIABLE_PARSER)
public class StatementParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+* ;")
            .build();

    @Override
    public void parse(ParserInfo info) {
        ParserPipelineRegistry parserPipelineRegistry = info.getComponent(PandaComponents.PIPELINE_REGISTRY);
        ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.STATEMENT);

        AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, info);
        TokenizedSource source = hollows.getGap(0);

        SourceStream declarationStream = new PandaSourceStream(source);
        UnifiedParser statementParser = pipeline.handle(declarationStream);

        if (statementParser == null) {
            throw new PandaParserException("Cannot recognize block at line " + TokenUtils.getLine(source));
        }

        ParserInfo statementParserInfo = info.fork();
        statementParserInfo.setComponent(PandaComponents.SOURCE_STREAM, declarationStream);
        statementParser.parse(statementParserInfo);
    }

    @LocalCallback
    private class DeclarationParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            ParserPipelineRegistry parserPipelineRegistry = delegatedInfo.getComponent(PandaComponents.PIPELINE_REGISTRY);
            ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.STATEMENT);

            AbyssRedactorHollows hollows = AbyssPatternAssistant.extract(PATTERN, delegatedInfo);
            TokenizedSource source = hollows.getGap(0);

            SourceStream declarationStream = new PandaSourceStream(source);
            UnifiedParser statementParser = pipeline.handle(declarationStream);

            if (statementParser == null) {
                throw new PandaParserException("Cannot recognize statement at line " + TokenUtils.getLine(source));
            }

            ParserInfo statementParserInfo = delegatedInfo.fork();
            statementParserInfo.setComponent(PandaComponents.SOURCE_STREAM, declarationStream);
            statementParser.parse(statementParserInfo);
        }

    }

}
