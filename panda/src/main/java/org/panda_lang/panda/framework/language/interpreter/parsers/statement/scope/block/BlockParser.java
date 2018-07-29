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

package org.panda_lang.panda.framework.language.interpreter.parsers.statement.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.defaults.ContainerParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationAssistant;
import org.panda_lang.panda.framework.design.interpreter.parser.component.*;
import org.panda_lang.panda.framework.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parsers.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.framework.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.token.utils.TokenUtils;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.framework.language.interpreter.PandaSyntax;

@ParserRegistration(target = PandaPipelines.SCOPE, parserClass = BlockParser.class, handlerClass = BlockParserHandler.class, priority = PandaPriorities.SCOPE_BLOCK_PARSER)
public class BlockParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** { +* }")
            .build();

    @Override
    public void parse(ParserData data) {
        CasualParserGenerationAssistant.delegateImmediately(data, new BlockDeclarationCasualParserCallback());
    }

    @LocalCallback
    private static class BlockDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, delegatedData, "block-declaration", "block-body");

            PipelineRegistry pipelineRegistry = delegatedData.getComponent(UniversalComponents.PIPELINE);
            ParserPipeline pipeline = pipelineRegistry.getPipeline(PandaPipelines.BLOCK);

            TokenizedSource blockDeclaration = redactor.get("block-declaration");
            SourceStream declarationStream = new PandaSourceStream(blockDeclaration);
            UnifiedParser blockParser = pipeline.handle(declarationStream);

            if (blockParser == null) {
                throw new PandaParserException("Cannot recognize block at line " + TokenUtils.getLine(blockDeclaration));
            }

            ParserData blockParserData = delegatedData.fork();
            blockParserData.setComponent(UniversalComponents.SOURCE_STREAM, declarationStream);
            blockParser.parse(blockParserData);

            Block block = blockParserData.getComponent(BlockComponents.BLOCK);
            Boolean unlistedBlock = blockParserData.getComponent(BlockComponents.UNLISTED_BLOCK);
            boolean unlisted = unlistedBlock != null && unlistedBlock;

            if (block == null) {
                throw new PandaParserException("Cannot find the result of block parsers (" + blockParser.getClass() + ")");
            }

            if (!unlisted) {
                Container container = delegatedData.getComponent(PandaComponents.CONTAINER);
                container.addStatement(block);
            }

            ParserData parentInfo = delegatedData.getComponent(UniversalComponents.PARENT_DATA);
            parentInfo.setComponent(BlockComponents.PREVIOUS_BLOCK, block);

            delegatedData.setComponent(BlockComponents.BLOCK, block);
            nextLayer.delegate(new BlockBodyCasualParserCallback(redactor), delegatedData);
        }

    }

    @LocalCallback
    private static class BlockBodyCasualParserCallback implements CasualParserGenerationCallback {

        private final AbyssRedactor redactor;

        public BlockBodyCasualParserCallback(AbyssRedactor redactor) {
            this.redactor = redactor;
        }

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            Container container = delegatedData.getComponent(BlockComponents.BLOCK);
            TokenizedSource body = redactor.get("block-body");

            ContainerParser containerParser = new ContainerParser(container);
            containerParser.parse(delegatedData, body);
        }

    }

}
