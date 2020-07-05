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

package org.panda_lang.panda.language.resource.syntax.scope.block.conditional;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Block;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.design.interpreter.source.Location;
import org.panda_lang.framework.language.architecture.expression.PandaExpression;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ExpressionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SubPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.block.BlockComponents;
import org.panda_lang.panda.language.interpreter.parser.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.block.BlockSubparserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.initializers.CustomPatternInitializer;

@RegistrableParser(pipeline = PandaPipeline.BLOCK_LABEL)
public final class ConditionalBlockParser extends BlockSubparserBootstrap {

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.IF, Keywords.ELSE))
                .initializer(new CustomPatternInitializer())
                .pattern(CustomPattern.of(
                        VariantElement.create("variant").content(
                                SubPatternElement.create("else if").of(
                                        KeywordElement.create(Keywords.ELSE),
                                        KeywordElement.create(Keywords.IF),
                                        ExpressionElement.create("condition").map(ExpressionTransaction::getExpression)
                                ),
                                SubPatternElement.create("if").of(
                                        KeywordElement.create(Keywords.IF),
                                        ExpressionElement.create("condition").map(ExpressionTransaction::getExpression)
                                ),
                                KeywordElement.create(Keywords.ELSE)
                        )
                ));
    }

    @Autowired(order = 1)
    BlockData parse(
        Context context,
        @Ctx Scope parent,
        @Ctx TypeLoader loader,
        @Ctx(BlockComponents.PREVIOUS_BLOCK_LABEL) Block previous,
        @Channel Result result,
        @Channel Location location
    ) {
        Expression condition = result.has("condition") ? result.get("condition") : new PandaExpression(loader.requireType(Boolean.class), true);
        ConditionalBlock conditionalBlock = new ConditionalBlock(parent, location, condition);

        if (result.has("else")) {
            if (!(previous instanceof ConditionalBlock)) {
                throw new PandaParserFailure(context, "The Else-block without associated If-block");
            }

            ConditionalBlock previousConditionalBlock = (ConditionalBlock) previous;
            previousConditionalBlock.setElseBlock(conditionalBlock);
            return new BlockData(conditionalBlock, true);
        }

        if (result.has("if")) {
            return new BlockData(conditionalBlock);
        }

        throw new PandaParserFailure(context, "Unrecognized condition type");
    }

}
