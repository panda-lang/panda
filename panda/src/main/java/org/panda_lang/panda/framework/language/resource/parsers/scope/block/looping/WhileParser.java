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

package org.panda_lang.panda.framework.language.resource.parsers.scope.block.looping;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.handlers.TokenHandler;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.WhileBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockData;
import org.panda_lang.panda.framework.language.resource.parsers.scope.block.BlockSubparserBootstrap;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK_LABEL)
public class WhileParser extends BlockSubparserBootstrap {

    @Override
    protected BootstrapParserBuilder<BlockData> initialize(ParserData data, BootstrapParserBuilder<BlockData> defaultBuilder) {
        return defaultBuilder
                .handler(new TokenHandler(Keywords.WHILE))
                .pattern("while `( <*content> `)");
    }

    @Autowired
    private BlockData parseWhile(ParserData data, @Src("*content") Snippet contentSource) {
        Expression expression = data.getComponent(UniversalComponents.EXPRESSION).parse(data, contentSource);

        if (!expression.getReturnType().isClassOf("Boolean")) {
            throw new PandaParserException("Loop requires boolean as an argument");
        }

        return new BlockData(new WhileBlock(expression));
    }

}
