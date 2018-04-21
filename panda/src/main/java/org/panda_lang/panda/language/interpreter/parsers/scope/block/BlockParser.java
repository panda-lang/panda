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

package org.panda_lang.panda.language.interpreter.parsers.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.design.interpreter.parser.defaults.ContainerParser;
import org.panda_lang.panda.design.interpreter.parser.generation.CasualParserGenerationAssistant;
import org.panda_lang.panda.language.interpreter.parsers.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parsers.PandaPriorities;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternAssistant;
import org.panda_lang.panda.design.interpreter.token.AbyssPatternBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
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
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.AbyssRedactor;
import org.panda_lang.panda.language.interpreter.PandaSyntax;

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
            delegatedData.setComponent("redactor", redactor);

            ParserPipelineRegistry parserPipelineRegistry = delegatedData.getComponent(PandaComponents.PIPELINE_REGISTRY);
            ParserPipeline pipeline = parserPipelineRegistry.getPipeline(PandaPipelines.BLOCK);

            TokenizedSource blockDeclaration = redactor.get("block-declaration");
            SourceStream declarationStream = new PandaSourceStream(blockDeclaration);
            UnifiedParser blockParser = pipeline.handle(declarationStream);

            if (blockParser == null) {
                throw new PandaParserException("Cannot recognize block at line " + TokenUtils.getLine(blockDeclaration));
            }

            ParserData blockParserData = delegatedData.fork();
            blockParserData.setComponent(PandaComponents.SOURCE_STREAM, declarationStream);
            blockParser.parse(blockParserData);

            Block block = blockParserData.getComponent("block");
            Object o = blockParserData.getComponent("block-unlisted");
            boolean unlisted = o != null && (boolean) o;

            if (block == null) {
                throw new PandaParserException("Cannot find the result of block parsers (" + blockParser.getClass() + ")");
            }

            if (!unlisted) {
                Container container = delegatedData.getComponent("container");
                container.addStatement(block);
            }

            ParserData parentInfo = delegatedData.getComponent(PandaComponents.PARENT_INFO);
            parentInfo.setComponent("previous-block", block);

            delegatedData.setComponent("block", block);
            nextLayer.delegate(new BlockBodyCasualParserCallback(), delegatedData);
        }

    }

    @LocalCallback
    private static class BlockBodyCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserData delegatedData, CasualParserGenerationLayer nextLayer) {
            Container container = delegatedData.getComponent("block");

            AbyssRedactor redactor = delegatedData.getComponent("redactor");
            TokenizedSource body = redactor.get("block-body");

            ContainerParser containerParser = new ContainerParser(container);
            containerParser.parse(delegatedData, body);
        }

    }

}
