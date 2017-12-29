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

package org.panda_lang.panda.language.structure.scope.block;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.defaults.ContainerParser;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPriorities;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.dynamic.Block;
import org.panda_lang.panda.core.structure.wrapper.Container;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.implementation.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.casual.CasualParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.registry.ParserPipelineRegistry;
import org.panda_lang.panda.framework.language.interpreter.token.TokenUtils;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.language.syntax.PandaSyntax;

@ParserRegistration(target = DefaultPipelines.SCOPE, parserClass = BlockParser.class, handlerClass = BlockParserHandler.class, priority = DefaultPriorities.BLOCK_PARSER)
public class BlockParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder().compile(PandaSyntax.getInstance(), "+* { +* }").build();

    @Override
    public void parse(ParserInfo info) {
        CasualParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(CasualParserGenerationType.HIGHER)
                .delegateImmediately(new BlockDeclarationCasualParserCallback(), info.fork());
    }

    @LocalCallback
    private static class BlockDeclarationCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenHollowRedactor redactor = new TokenHollowRedactor(hollows);

            redactor.map("block-declaration", "block-body");
            delegatedInfo.setComponent("redactor", redactor);

            ParserPipelineRegistry parserPipelineRegistry = delegatedInfo.getComponent(Components.PIPELINE_REGISTRY);
            ParserPipeline pipeline = parserPipelineRegistry.getPipeline(DefaultPipelines.BLOCK);

            TokenizedSource blockDeclaration = redactor.get("block-declaration");
            SourceStream declarationStream = new PandaSourceStream(blockDeclaration);
            UnifiedParser blockParser = pipeline.handle(declarationStream);

            if (blockParser == null) {
                throw new PandaParserException("Cannot recognize block at line " + TokenUtils.getLine(blockDeclaration));
            }

            ParserInfo blockParserInfo = delegatedInfo.fork();
            blockParserInfo.setComponent(Components.SOURCE_STREAM, declarationStream);

            blockParser.parse(blockParserInfo);

            Block block = blockParserInfo.getComponent("block");
            Object o = blockParserInfo.getComponent("block-unlisted");
            boolean unlisted = o != null && (boolean) o;

            if (block == null) {
                throw new PandaParserException("Cannot find the result of block parser (" + blockParser.getClass() + ")");
            }

            if (!unlisted) {
                Container container = delegatedInfo.getComponent("container");
                container.addStatement(block);
            }

            ParserInfo parentInfo = delegatedInfo.getComponent(Components.PARENT_INFO);
            parentInfo.setComponent("previous-block", block);

            delegatedInfo.setComponent("block", block);
            nextLayer.delegate(new BlockBodyCasualParserCallback(), delegatedInfo);
        }

    }

    @LocalCallback
    private static class BlockBodyCasualParserCallback implements CasualParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, CasualParserGenerationLayer nextLayer) {
            Container container = delegatedInfo.getComponent("block");

            TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
            TokenizedSource body = redactor.get("block-body");

            ContainerParser containerParser = new ContainerParser(container);
            containerParser.parse(delegatedInfo, body);
        }

    }

}
