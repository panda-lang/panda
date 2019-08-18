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

package org.panda_lang.panda.framework.design.interpreter.parser.block;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.ParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Local;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.data.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.UniversalPipelines;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;
import org.panda_lang.panda.framework.language.resource.parsers.ContainerParser;

import java.util.function.Supplier;

@Registrable(pipeline = UniversalPipelines.CONTAINER_LABEL, priority = PandaPriorities.CONTAINER_BLOCK)
public class BlockParser extends ParserBootstrap {

    private final ContainerParser containerParser = new ContainerParser();

    @Override
    protected BootstrapInitializer initialize(Context context, BootstrapInitializer initializer) {
        return initializer.pattern("<*declaration> body:~{");
    }

    @Override
    protected Boolean customHandle(ParserHandler handler, Context context, Snippet source) {
        HandleResult<BlockSubparser> result = context.getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.BLOCK)
                .handle(context, source);

        return result.isFound();
    }

    @Autowired(order = 1)
    void parse(Context context, LocalData local, @Src("*declaration") Snippet declaration) throws Exception {
        SourceStream declarationStream = new PandaSourceStream(declaration);

        HandleResult<BlockSubparser> handleResult = context
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.BLOCK)
                .handle(context, declarationStream.toSnippet());

        BlockSubparser blockParser = handleResult.getParser().orElseThrow((Supplier<? extends Exception>) () -> {
            if (handleResult.getFailure().isPresent()) {
                throw handleResult.getFailure().get();
            }

            throw PandaParserFailure.builder("Unknown block", context)
                    .withStreamOrigin(declaration)
                    .build();
        });

        Context delegatedContext = local.allocated(context.fork());
        BlockData blockData = blockParser.parse(delegatedContext, declaration);

        if (blockData == null || blockData.getBlock() == null) {
            throw PandaParserFailure.builder(blockParser.getClass().getSimpleName() + " cannot parse current block", context)
                    .withStreamOrigin(declaration)
                    .build();
        }

        local.allocated(blockData.getBlock());

        if (blockData.isUnlisted()) {
            return;
        }

        context.getComponent(UniversalComponents.CONTAINER).addStatement(blockData.getBlock());
        context.withComponent(BlockComponents.PREVIOUS_BLOCK, blockData.getBlock());
    }

    @Autowired(order = 2)
    void parseContent(@Local Context blockContext, @Local Block block, @Nullable @Src("body") Snippet body) throws Exception {
        if (body != null) {
            containerParser.parse(blockContext, block, body);
        }
    }

}
