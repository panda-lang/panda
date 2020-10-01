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
import org.panda_lang.language.Failure;
import org.panda_lang.language.architecture.statement.Block;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.architecture.statement.Statement;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.pool.ParserPool;
import org.panda_lang.language.interpreter.parser.pool.Target;
import org.panda_lang.language.interpreter.parser.pool.Targets;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.ScopeParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredInitializer;
import org.panda_lang.panda.language.interpreter.parser.autowired.AutowiredParser;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.autowired.annotations.Src;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

public final class BlockParser extends AutowiredParser<Void> {

    private static final ScopeParser SCOPE_PARSER = new ScopeParser();
    private ParserPool<BlockSubparser> parserPool;

    @Override
    public Target<? extends Parser>[] pipeline() {
        return ArrayUtils.of(Targets.SCOPE);
    }

    @Override
    public double priority() {
        return PandaPriorities.SCOPE_BLOCK;
    }

    @Override
    protected AutowiredInitializer<Void> initialize(Context context, AutowiredInitializer<Void> initializer) {
        this.parserPool = context.getComponent(Components.PIPELINE).getPipeline(PandaPipeline.BLOCK);

        return initializer.functional(builder -> builder
                .contentBefore("declaration", Separators.BRACE_LEFT)
                .section("body", Separators.BRACE_LEFT));
    }

    @Override
    protected Boolean customHandle(Handler handler, Context context, LocalChannel channel, Snippet source) {
        Result<BlockSubparser, Option<Failure>> result = parserPool.handle(context, channel, source);
        result.peek(parser -> channel.allocated("subparser", parser));
        return result.isOk();
    }

    @Autowired(order = 1)
    public void parse(Context context, LocalChannel channel, @Ctx Scope parent, @Src("declaration") Snippet declaration) {
        BlockSubparser blockParser = channel.get("subparser", BlockSubparser.class);

        Context delegatedContext = channel.allocated("blockContext", context.fork())
                .withComponent(Components.CHANNEL, new PandaLocalChannel());

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

        channel.allocated("block", blockData.getBlock());

        if (!blockData.isUnlisted()) {
            parent.addStatement(blockData.getBlock());
        }

        parent.addStatement(new BlockStatement(blockData.getBlock()));
    }

    @Autowired(order = 2)
    public void parseContent(@Channel Context blockContext, @Channel Block block, @Nullable @Src("body") Snippet body) {
        if (body != null) {
            SCOPE_PARSER.parse(blockContext, block, body);
        }
    }

}
