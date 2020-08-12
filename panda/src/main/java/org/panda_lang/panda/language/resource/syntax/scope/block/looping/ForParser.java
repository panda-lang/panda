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

package org.panda_lang.panda.language.resource.syntax.scope.block.looping;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.architecture.module.ModuleLoaderUtils;
import org.panda_lang.language.architecture.statement.Scope;
import org.panda_lang.language.interpreter.parser.Components;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.language.interpreter.parser.pipeline.PipelineComponent;
import org.panda_lang.language.interpreter.source.Location;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.architecture.expression.PandaExpression;
import org.panda_lang.language.architecture.statement.PandaBlock;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.resource.syntax.keyword.Keywords;
import org.panda_lang.language.resource.syntax.separator.Separators;
import org.panda_lang.panda.language.interpreter.parser.PandaPipeline;
import org.panda_lang.panda.language.interpreter.parser.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.block.BlockSubparserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.utilities.commons.ArrayUtils;

public final class ForParser extends BlockSubparserBootstrap {

    private Expression defaultCondition;

    @Override
    public PipelineComponent<? extends Parser>[] pipeline() {
        return ArrayUtils.of(PandaPipeline.BLOCK);
    }

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        this.defaultCondition = new PandaExpression(ModuleLoaderUtils.requireType(context, boolean.class), true);

        return initializer
                .handler(new TokenHandler(Keywords.FOR))
                .linear("for content:(~)");
    }

    @Autowired(order = 1)
    public BlockData parseBlock(Context context, @Ctx Scope parent, @Channel Location location, @Src("content") Snippet content, @Ctx ExpressionParser expressionParser) {
        Snippet[] forEachElements = content.split(Separators.SEMICOLON);

        if (forEachElements.length != 3) {
            throw new PandaParserFailure(context, content,
                    "Invalid amount of statements in for loop declaration",
                    "The statement should look like: for (<initialization>; <termination>; <increment>)"
            );
        }

        Scope forBlock = new PandaBlock(parent, location);
        Context delegatedContext = context.fork().withComponent(Components.SCOPE, forBlock);

        Snippet initializationSource = forEachElements[0];
        Expression initialization = null;

        if (!initializationSource.isEmpty()) {
            initialization = expressionParser.parse(delegatedContext, initializationSource).getExpression();
        }

        Snippet terminationSource = forEachElements[1];
        Expression termination = defaultCondition;

        if (!terminationSource.isEmpty()) {
            termination = expressionParser.parse(delegatedContext, terminationSource).getExpression();
        }

        Snippet incrementSource = forEachElements[2];
        Expression increment = null;

        if (!incrementSource.isEmpty()) {
            increment = expressionParser.parse(delegatedContext, incrementSource).getExpression();
        }

        return new BlockData(new ForBlock(forBlock, content.getLocation(), initialization, termination, increment));
    }

}
