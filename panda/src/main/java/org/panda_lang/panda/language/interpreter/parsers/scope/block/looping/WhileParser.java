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

package org.panda_lang.panda.language.interpreter.parsers.scope.block.looping;

import org.panda_lang.panda.design.architecture.dynamic.looping.*;
import org.panda_lang.panda.design.interpreter.parser.pipeline.registry.*;
import org.panda_lang.panda.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.interpreter.parser.*;
import org.panda_lang.panda.framework.design.interpreter.token.*;
import org.panda_lang.panda.framework.design.runtime.expression.*;
import org.panda_lang.panda.framework.language.interpreter.parser.*;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.*;
import org.panda_lang.panda.framework.language.interpreter.token.pattern.abyss.redactor.*;
import org.panda_lang.panda.language.interpreter.*;
import org.panda_lang.panda.language.interpreter.parsers.*;
import org.panda_lang.panda.language.interpreter.parsers.general.expression.*;
import org.panda_lang.panda.language.interpreter.parsers.scope.block.*;

@ParserRegistration(target = PandaPipelines.BLOCK, parserClass = WhileParser.class, handlerClass = WhileParserHandler.class)
public class WhileParser implements UnifiedParser {

    protected static final AbyssPattern PATTERN = new AbyssPatternBuilder()
            .compile(PandaSyntax.getInstance(), "while ( +* )")
            .build();

    @Override
    public void parse(ParserData data) {
        AbyssRedactor redactor = AbyssPatternAssistant.traditionalMapping(PATTERN, data, "while-expression");
        TokenizedSource expressionSource = redactor.get("while-expression");

        ExpressionParser expressionParser = new ExpressionParser();
        Expression expression = expressionParser.parse(data, expressionSource);

        if (!expression.getReturnType().isClassOf("Boolean")) {
            throw new PandaParserException("Loop requires boolean as an argument");
        }

        data.setComponent(BlockComponents.BLOCK, new WhileBlock(expression));
    }

}
