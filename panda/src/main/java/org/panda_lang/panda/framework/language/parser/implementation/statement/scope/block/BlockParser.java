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

package org.panda_lang.panda.framework.language.parser.implementation.statement.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.architecture.statement.Container;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserRepresentation;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.interpreter.token.distributor.SourceStream;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.AbyssPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.abyss.utils.AbyssPatternBuilder;
import org.panda_lang.panda.framework.language.interpreter.token.PandaSyntax;
import org.panda_lang.panda.framework.language.interpreter.token.distributor.PandaSourceStream;
import org.panda_lang.panda.framework.language.parser.bootstrap.PandaParserBootstrap;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.language.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.Delegation;
import org.panda_lang.panda.framework.language.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.language.parser.implementation.ContainerParser;

@ParserRegistration(target = PandaPipelines.SCOPE, parserClass = BlockParser.class, handlerClass = BlockParserHandler.class, priority = PandaPriorities.SCOPE_BLOCK_PARSER)
public class BlockParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "+** { +* }")
            .build();

    protected ParserRepresentation bootstrapParser = PandaParserBootstrap.builder()
            .pattern("+** { +* }", "block-declaration", "block-body")
            .instance(this)
            .build();

    @Override
    public boolean parse(ParserData data) {
        return bootstrapParser.getParser().parse(data);
    }

    @Autowired(value = Delegation.IMMEDIATELY, order = 1)
    private void parseDeclaration(ParserData data, LocalData localData, @Component PipelineRegistry registry, @Redactor("block-declaration") TokenizedSource blockDeclaration) {
        ParserPipeline pipeline = registry.getPipeline(PandaPipelines.BLOCK);

        SourceStream declarationStream = new PandaSourceStream(blockDeclaration);
        UnifiedParser blockParser = pipeline.handle(declarationStream);

        if (blockParser == null) {
            throw new PandaParserFailure("Unknown block", data);
        }

        ParserData blockData = data.fork();
        blockData.setComponent(UniversalComponents.SOURCE_STREAM, declarationStream);
        blockParser.parse(blockData);

        Block block = localData.allocateInstance(Block.class, blockData.getComponent(BlockComponents.BLOCK));
        Boolean unlisted = blockData.getComponent(BlockComponents.UNLISTED_BLOCK);

        if (block == null) {
            throw new PandaParserFailure(blockParser.getClass() + " cannot parse current block", data);
        }

        data.setComponent(BlockComponents.BLOCK, block);

        if (unlisted != null && !unlisted) {
            data.getComponent(PandaComponents.CONTAINER).addStatement(block);
        }

        data.getComponent(UniversalComponents.PARENT_DATA).setComponent(BlockComponents.PREVIOUS_BLOCK, block);
    }

    @Autowired(order = 2)
    private void parseContent(ParserData data, @Component Container container, @Redactor("block-body") TokenizedSource blockBody) {
        ContainerParser containerParser = new ContainerParser(container);
        containerParser.parse(data, blockBody);
    }

}
