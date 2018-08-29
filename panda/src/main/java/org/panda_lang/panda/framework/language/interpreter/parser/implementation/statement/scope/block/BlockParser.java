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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.generation.casual.GenerationLayer;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.token.stream.TokenReader;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.ContainerParser;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

@ParserRegistration(target = PandaPipelines.SCOPE, priority = PandaPriorities.SCOPE_BLOCK_PARSER)
public class BlockParser implements UnifiedParser, ParserHandler {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** { +* }")
            .build();

    protected ParserRepresentation bootstrapParser = PandaParserBootstrap.builder()
            .pattern("+** { +* }", "block-declaration", "block-body")
            .instance(this)
            .build();

    @Override
    public boolean handle(TokenReader reader) {
        return PATTERN.extractor().extract(reader) != null;
    }

    @Override
    public boolean parse(ParserData data, GenerationLayer nextLayer) {
        return bootstrapParser.getParser().parse(data, nextLayer);
    }

    @Autowired(order = 1)
    private void parse(ParserData data, LocalData local, GenerationLayer layer, @Component PipelineRegistry registry, @Redactor("block-declaration") TokenizedSource declaration) {
        ParserPipeline pipeline = registry.getPipeline(PandaPipelines.BLOCK);

        SourceStream declarationStream = new PandaSourceStream(declaration);
        UnifiedParser blockParser = pipeline.handle(declarationStream);

        if (blockParser == null) {
            throw new PandaParserFailure("Unknown block", data);
        }

        ParserData blockData = local.allocateInstance(data.fork());
        blockData.setComponent(UniversalComponents.SOURCE_STREAM, declarationStream);
        blockParser.parse(blockData, layer);

        Block block = local.allocateInstance(blockData.getComponent(BlockComponents.BLOCK));
        Boolean unlisted = blockData.getComponent(BlockComponents.UNLISTED_BLOCK);

        if (block == null) {
            throw new PandaParserFailure(blockParser.getClass() + " cannot parse current block", data);
        }

        if (unlisted == null || !unlisted) {
            data.getComponent(PandaComponents.CONTAINER).addStatement(block);
        }

        data.setComponent(BlockComponents.PREVIOUS_BLOCK, block);
    }

    @Autowired(order = 2)
    private void parseContent(@Local ParserData blockData, @Local Block block, @Redactor("block-body") TokenizedSource body) {
        ContainerParser containerParser = new ContainerParser(block);
        containerParser.parse(blockData, body);
    }

}
