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

import org.panda_lang.panda.framework.design.architecture.dynamic.Scope;
import org.panda_lang.panda.framework.design.architecture.statement.VariableData;
import org.panda_lang.panda.framework.design.interpreter.parser.Context;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.design.interpreter.token.Snippet;
import org.panda_lang.panda.language.architecture.statement.PandaVariable;
import org.panda_lang.panda.language.architecture.statement.VariableDataInitializer;
import org.panda_lang.panda.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.block.BlockData;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.block.BlockSubparserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Component;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.handlers.TokenHandler;
import org.panda_lang.panda.language.interpreter.parser.bootstraps.context.interceptors.LinearPatternInterceptor;
import org.panda_lang.panda.language.interpreter.parser.loader.Registrable;
import org.panda_lang.panda.language.resource.PandaTypes;
import org.panda_lang.panda.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.resource.syntax.operator.Operators;

@Registrable(pipeline = PandaPipelines.BLOCK_LABEL)
public class ForEachParser extends BlockSubparserBootstrap {
    @Override
    protected BootstrapInitializer<BlockData> initialize(Context context, BootstrapInitializer<BlockData> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.FOREACH))
                .interceptor(new LinearPatternInterceptor())
                .pattern("foreach content:(~)");
    }

    @Autowired
    BlockData parseBlock(Context context, @Component Scope parent, @Component ExpressionParser parser, @Src("content") Snippet content) {
        Snippet[] elements = content.split(Operators.COLON);

        if (elements.length != 2) {
            throw PandaParserFailure.builder("Invalid amount of statements in for each loop declaration", context)
                    .withStreamOrigin(content)
                    .withNote("The statement should look like: foreach (<value> : <source>)")
                    .build();
        }

        ForEachBlock forEach = new ForEachBlock(parent, parser.parse(context, elements[1]));
        VariableDataInitializer dataInitializer = new VariableDataInitializer(context, forEach);
        VariableData variableData = dataInitializer.createVariableData(elements[0], true, true);
        forEach.addVariable(new PandaVariable(forEach.getValuePointer(), variableData));

        if (!PandaTypes.ITERABLE.isAssignableFrom(forEach.getIterableExpression().getReturnType())) {
            throw new PandaParserException("ForEach requires Iterable value");
        }

        return new BlockData(forEach);
    }

}
