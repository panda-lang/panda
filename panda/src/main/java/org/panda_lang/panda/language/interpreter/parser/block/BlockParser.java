/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.panda.language.interpreter.parser.block;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.Statement;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Channel;
import org.panda_lang.framework.design.interpreter.parser.pipeline.HandleResult;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ContentBeforeElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SectionElement;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Cache;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.data.LocalCache;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_BLOCK)
public final class BlockParser extends ParserBootstrap<Void> {

    private static final ScopeParser SCOPE_PARSER = new ScopeParser();

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        ContentBeforeElement.create("declaration").before(Separators.BRACE_LEFT),
                        SectionElement.create("body")
                ));
    }

    @Override
    protected Boolean customHandle(Handler handler, Context context, Channel channel, Snippet source) {
        HandleResult<BlockSubparser> result = context.getComponent(Components.PIPELINE)
                .getPipeline(PandaPipeline.BLOCK)
                .handle(context, channel, source);

        channel.put("result", result.getParser().getOrNull());
        return result.getParser().isPresent();
    }

    @Autowired(order = 1)
    void parse(Context context, LocalCache local, @Ctx Channel channel, @Ctx Scope parent, @Src("declaration") Snippet declaration) throws Exception {
        BlockSubparser blockParser = channel.get("result", BlockSubparser.class);
        Context delegatedContext = local.allocated(context.fork()).withComponent(Components.CHANNEL, channel);

        if (!parent.getStatements().isEmpty()) {
            Statement statement = parent.getStatements().get(parent.getStatements().size() - 1);

            if (statement instanceof BlockStatement) {
                delegatedContext.withComponent(BlockComponents.PREVIOUS_BLOCK, ((BlockStatement) statement).getBlock());
            }
        }

        BlockData blockData = blockParser.parse(delegatedContext, declaration);

        if (blockData == null || blockData.getBlock() == null) {
            throw new PandaParserFailure(context, declaration, blockParser.getClass().getSimpleName() + " cannot parse current block");
        }

        local.allocated(blockData.getBlock());

        if (!blockData.isUnlisted()) {
            parent.addStatement(blockData.getBlock());
        }

        parent.addStatement(new BlockStatement(blockData.getBlock()));
    }

    @Autowired(order = 2)
    void parseContent(@Cache Context blockContext, @Cache Scope block, @Nullable @Src("body") Snippet body) throws Exception {
        if (body != null) {
            SCOPE_PARSER.parse(blockContext, block, body);
        }
    }

}
