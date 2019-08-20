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

package org.panda_lang.panda.framework.language.resource.container.block.conditional;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapInitializer;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Inter;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.block.BlockComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.block.BlockData;
import org.panda_lang.panda.framework.language.interpreter.parser.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = PandaPipelines.BLOCK_LABEL)
public class ConditionalBlockParser extends BlockSubparserBootstrap {

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.IF, Keywords.ELSE))
                .pattern("((if:if|elseif:else if) <condition:reader expression>|else:else)");
    }

    @Autowired
    BlockData parse(Context context, @Inter ExtractorResult result, @Component Context parentContext, @Src("condition") @Nullable Expression condition) {
        if (result.hasIdentifier("else")) {
            ElseBlock elseBlock = new ElseBlock();
            Block previousBlock = parentContext.getComponent(BlockComponents.PREVIOUS_BLOCK);

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw PandaParserFailure.builder("The Else-block without associated If-block", context)
                        .withSourceFragment()
                            .ofOriginals(parentContext)
                            .create()
                        .build();
            }

            ConditionalBlock conditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(elseBlock);

            return new BlockData(elseBlock, true);
        }

        if (condition == null) {
            throw PandaParserFailure.builder("Empty condition", context)
                    .withSourceFragment()
                        .ofOriginals(parentContext)
                        .create()
                    .build();
        }

        ConditionalBlock conditionalBlock = new ConditionalBlock(condition);

        if (result.hasIdentifier("if")) {
            return new BlockData(conditionalBlock);
        }

        if (result.hasIdentifier("elseif")) {
            Block previousBlock = parentContext.getComponent(BlockComponents.PREVIOUS_BLOCK);

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw PandaParserFailure.builder("The If-Else-block without associated If-block", context)
                        .withSourceFragment()
                            .ofOriginals(parentContext)
                            .create()
                        .build();
            }

            ConditionalBlock previousConditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(previousConditionalBlock);
            return new BlockData(previousBlock, true);
        }

       // throw new PandaParserFailure("Unrecognized condition type", context);
        return null;
    }

}
