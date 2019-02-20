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

package org.panda_lang.panda.interpreter.parser.implementation.general.expression.fixed;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionParser;
import org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.ExpressionSubparsersLoader;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class ExpressionParserTest {

    private static ExpressionParser expressionParser;

    @BeforeAll
    public static void load() throws Exception {
        expressionParser = new ExpressionParser(new ExpressionSubparsersLoader().load());
    }

    @BeforeEach
    public void emptyLine() {
        System.out.println(StringUtils.EMPTY);
    }

    @Test
    public void parseSequences() {
        parse("'hello panda'");
        parse("'hello panda' 'hello panda'");
    }

    @Test
    public void parseLiterals() {
        parse("null");
        parse("true false");
    }

    @Test
    public void parseSection() {
        parse("()");
        parse("('chance')");
    }

    private static void parse(String source) {
        System.out.println(source + ": " + expressionParser.parse(null, PandaLexerUtils.convert(source)));
    }

}
