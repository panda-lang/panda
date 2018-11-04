/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block.looping;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.dynamic.block.looping.WhileBlock;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.BootstrapParser;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.annotations.Redactor;
import org.panda_lang.panda.framework.language.interpreter.parser.bootstrap.handlers.FirstTokenHandler;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.statement.scope.block.BlockComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.pipeline.ParserRegistration;
import org.panda_lang.panda.framework.language.resource.syntax.keyword.Keywords;

@ParserRegistration(target = PandaPipelines.BLOCK)
public class WhileParser extends BootstrapParser {

    {
        parserBuilder = builder()
                .handler(new FirstTokenHandler(Keywords.WHILE))
                .pattern("while ( +* )", "while-expression");
    }

    @Autowired
    private void parserWhile(ParserData data, @Redactor("while-expression") TokenizedSource expressionSource) {
        Expression expression = new ExpressionParser().parse(data, expressionSource);

        if (!expression.getReturnType().isClassOf("Boolean")) {
            throw new PandaParserException("Loop requires boolean as an argument");
        }

        data.setComponent(BlockComponents.BLOCK, new WhileBlock(expression));
    }

}
