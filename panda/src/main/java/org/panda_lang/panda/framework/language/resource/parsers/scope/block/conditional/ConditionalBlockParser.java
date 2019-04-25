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

package org.panda_lang.panda.framework.language.resource.parsers.scope.block.conditional;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.dynamic.Block;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.pattern.descriptive.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.SnippetUtils;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.conditional.ConditionalBlock;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.conditional.ElseBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockComponents;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockData;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK_LABEL)
public class ConditionalBlockParser extends BlockSubparserBootstrap {

    @Override
    protected BootstrapParserBuilder<BlockData> initialize(ParserData data, BootstrapParserBuilder<BlockData> defaultBuilder) {
        return defaultBuilder
                .handler(new TokenHandler(Keywords.IF, Keywords.ELSE))
                .pattern("((if:if|elseif:else if) `( <*condition> `)|else:else)");
    }

    @Autowired
    public BlockData parse(ParserData data, ExtractorResult pattern, @Component ParserData parentData, @Src("*condition") @Nullable Snippet condition) {
        if (pattern.hasIdentifier("else")) {
            ElseBlock elseBlock = new ElseBlock();
            Block previousBlock = parentData.getComponent(BlockComponents.PREVIOUS_BLOCK);

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw new PandaParserFailure("The Else-block without associated If-block", data);
            }

            ConditionalBlock conditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(elseBlock);

            return new BlockData(elseBlock, true);
        }

        if (SnippetUtils.isEmpty(condition)) {
            throw new PandaParserFailure("Empty condition", data);
        }

        Expression expression = data.getComponent(UniversalComponents.EXPRESSION).parse(data, condition);
        ConditionalBlock conditionalBlock = new ConditionalBlock(expression);

        if (pattern.hasIdentifier("if")) {
            return new BlockData(conditionalBlock);
        }

        if (pattern.hasIdentifier("elseif")) {
            Block previousBlock = parentData.getComponent(BlockComponents.PREVIOUS_BLOCK);

            if (!(previousBlock instanceof ConditionalBlock)) {
                throw new PandaParserFailure("The If-Else-block without associated If-block", data);
            }

            ConditionalBlock previousConditionalBlock = (ConditionalBlock) previousBlock;
            conditionalBlock.setElseBlock(previousConditionalBlock);
            return new BlockData(previousBlock, true);
        }

        throw new PandaParserFailure("Unrecognized condition type", data);
    }

}
