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
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Inter;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.block.BlockSubparserBootstrap;
import org.panda_lang.framework.language.resource.PandaTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;

@Registrable(pipeline = PandaPipelines.BLOCK_LABEL)
public class LoopParser extends BlockSubparserBootstrap {

    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LOOP))
                .interceptor(new LinearPatternInterceptor())
                .pattern("loop content:(~)");
    }

    @Autowired
    BlockData parseContent(Context context, @Component Scope parent, @Inter SourceLocation location, @Src("content") Snippet content) {
        Expression expression = context.getComponent(Components.EXPRESSION).parse(context, content).getExpression();

        if (!PandaTypes.INT.isAssignableFrom(expression.getReturnType())) {
            throw new PandaParserException("Loop requires number as an argument");
        }

        return new BlockData(new LoopBlock(parent, location, expression));
    }

}
