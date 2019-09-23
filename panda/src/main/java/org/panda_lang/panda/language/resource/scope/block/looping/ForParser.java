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

package org.panda_lang.panda.language.resource.scope.block.looping;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.architecture.statement.PandaBlock;
import org.panda_lang.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.block.BlockSubparserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.language.resource.PandaTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.framework.language.resource.syntax.separator.Separators;
import org.panda_lang.framework.language.architecture.expression.PandaExpression;

@Registrable(pipeline = PandaPipelines.BLOCK_LABEL)
public final class ForParser extends BlockSubparserBootstrap {

    private static final Expression DEFAULT_CONDITION = new PandaExpression(PandaTypes.BOOLEAN, true);

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.FOR))
                .interceptor(new LinearPatternInterceptor())
                .pattern("for content:(~)");
    }

    @Autowired
    BlockData parseBlock(Context context, @Component Scope parent, @Inter SourceLocation location, @Src("content") Snippet content, @Component ExpressionParser expressionParser) {
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
        Expression termination = DEFAULT_CONDITION;

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
