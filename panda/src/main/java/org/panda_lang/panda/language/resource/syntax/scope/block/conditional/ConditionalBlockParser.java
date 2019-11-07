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

package org.panda_lang.panda.language.resource.syntax.scope.block.conditional;

import org.panda_lang.framework.design.architecture.statement.Block;
import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionTransaction;
import org.panda_lang.framework.language.interpreter.pattern.custom.CustomPattern;
import org.panda_lang.framework.language.interpreter.pattern.custom.Result;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.ExpressionElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.KeywordElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.SubPatternElement;
import org.panda_lang.framework.language.interpreter.pattern.custom.elements.VariantElement;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.block.BlockComponents;
import org.panda_lang.panda.language.interpreter.parser.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.block.BlockSubparserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.context.interceptors.CustomPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;

@RegistrableParser(pipeline = PandaPipeline.BLOCK_LABEL)
public class ConditionalBlockParser extends BlockSubparserBootstrap {

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.IF, Keywords.ELSE))
                .interceptor(new CustomPatternInterceptor())
                .pattern(CustomPattern.of(
                        VariantElement.create("variant").content(
                                SubPatternElement.create("with-condition").of(
                                        VariantElement.create("type").content("if", "else if"),
                                        ExpressionElement.create("condition").map(ExpressionTransaction::getExpression)
                                ),
                                KeywordElement.create(Keywords.ELSE)
                        )
                ));
    }

    @Autowired
    BlockData parse(Context context, @Inter Result result, @Inter SourceLocation location, @Component Scope parent, @Component(BlockComponents.PREVIOUS_BLOCK_LABEL) Block previous) {
        if (result.has("else")) {
            ElseBlock elseBlock = new ElseBlock(parent, location);

            if (!(previous instanceof ConditionalBlock)) {
                throw new PandaParserFailure(context, "The Else-block without associated If-block");
            }

            ConditionalBlock conditionalBlock = (ConditionalBlock) previous;
            conditionalBlock.setElseBlock(elseBlock);
            return new BlockData(elseBlock, true);
        }

        if (!result.has("condition")) {
            throw new PandaParserFailure(context, "Empty condition");
        }

        ConditionalBlock conditionalBlock = new ConditionalBlock(parent, location, result.get("condition"));
        String type = result.get("type").toString();

        if (type.equals("if")) {
            return new BlockData(conditionalBlock);
        }

        if (type.equals("else if")) {
            if (!(previous instanceof ConditionalBlock)) {
                throw new PandaParserFailure(context, "The If-Else-block without associated If-block");
            }

            ConditionalBlock previousConditionalBlock = (ConditionalBlock) previous;
            conditionalBlock.setElseBlock(previousConditionalBlock);
            return new BlockData(previous, true);
        }

        throw new PandaParserFailure(context, "Unrecognized condition type");
    }

}
