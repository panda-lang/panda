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

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = StatementParser.class, handlerClass = StatementParserHandler.class, priority = DefaultPriorities.STATEMENT_VARIABLE_PARSER)
public class StatementParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .hollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        ParserPipelineRegistry parserPipelineRegistry = info.getComponent(Components.PIPELINE_REGISTRY);
        ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.STATEMENT);

        TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, info);
        TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);
        TokenizedSource source = hollows.getGap(0);

        SourceStream declarationStream = new PandaSourceStream(source);
        UnifiedParser statementParser = pipeline.handle(declarationStream);

        if (statementParser == null) {
            throw new PandaParserException("Cannot recognize block at line " + TokenUtils.getLine(source));
        }

        ParserInfo statementParserInfo = info.fork();
        statementParserInfo.setComponent(Components.SOURCE_STREAM, declarationStream);
        statementParser.parse(statementParserInfo);
    }

    private class DeclarationParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            ParserPipelineRegistry parserPipelineRegistry = delegatedInfo.getComponent(Components.PIPELINE_REGISTRY);
            ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.STATEMENT);

            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);
            TokenizedSource source = hollows.getGap(0);

            SourceStream declarationStream = new PandaSourceStream(source);
            UnifiedParser statementParser = pipeline.handle(declarationStream);

            if (statementParser == null) {
                throw new PandaParserException("Cannot recognize statement at line " + TokenUtils.getLine(source));
            }

            ParserInfo statementParserInfo = delegatedInfo.fork();
            statementParserInfo.setComponent(Components.SOURCE_STREAM, declarationStream);
            statementParser.parse(statementParserInfo);
        }

    }

}
