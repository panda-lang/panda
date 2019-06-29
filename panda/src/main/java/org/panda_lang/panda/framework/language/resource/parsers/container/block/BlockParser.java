/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.resource.parsers.container.block;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.parsers.ContainerParser;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

@ParserRegistration(pipeline = PandaPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_BLOCK)
public class BlockParser extends ParserBootstrap {

    private final ContainerParser containerParser = new ContainerParser();

    @Override
    protected BootstrapInitializer initialize(ParserData data, BootstrapInitializer initializer) {
        return initializer.pattern("<*declaration> body:~{");
    }

    @Override
    public boolean customHandle(ParserHandler handler, ParserData data, Snippet source) {
        return ObjectUtils.isNotNull(data
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.BLOCK)
                .handle(data, source));
    }

    @Autowired(order = 1)
    void parse(ParserData data, LocalData local, @Src("*declaration") Snippet declaration) throws Exception {
        SourceStream declarationStream = new PandaSourceStream(declaration);

        ParserPipeline<BlockSubparser> pipeline = data.getComponent(UniversalComponents.PIPELINE).getPipeline(PandaPipelines.BLOCK);
        BlockSubparser blockParser = pipeline.handle(data, declarationStream.toSnippet());

        if (blockParser == null) {
            throw PandaParserFailure.builder("Unknown block", data)
                    .withStreamOrigin(declaration)
                    .build();
        }

        ParserData delegatedData = local.allocated(data.fork());
        BlockData blockData = blockParser.parse(delegatedData, declaration);

        if (blockData == null || blockData.getBlock() == null) {
            throw PandaParserFailure.builder(blockParser.getClass().getSimpleName() + " cannot parse current block", data)
                    .withStreamOrigin(declaration)
                    .build();
        }

        local.allocated(blockData.getBlock());

        if (blockData.isUnlisted()) {
            return;
        }

        data.getComponent(PandaComponents.CONTAINER).addStatement(blockData.getBlock());
        data.setComponent(BlockComponents.PREVIOUS_BLOCK, blockData.getBlock());
    }

    @Autowired(order = 2)
    void parseContent(@Local ParserData blockData, @Local Block block, @Nullable @Src("body") Snippet body) throws Exception {
        if (body != null) {
            containerParser.parse(block, body, blockData);
        }
    }

}
