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

package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparsers;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparsersLoader;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionTokens;

import java.util.ArrayList;

class ExpressionParserTest {

    private static ExpressionParser PARSER;

    @Test
    public void testRead() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("true", read("true")),
                () -> Assertions.assertEquals("true", read("true false")),

                () -> Assertions.assertEquals("this.call(a,b)", read("this.call(a,b)")),
                () -> Assertions.assertEquals("this.get().call(a,b)", read("this.get().call(a,b) this.call(a,b)")),

                //() -> Assertions.assertEquals("newObject(){}", read("new Object(){}")),
                //() -> Assertions.assertEquals("newObject(){}.toString()", read("new Object(){}.toString() call()")),

                () -> Assertions.assertEquals("this.instance", read("this.instance")),
                () -> Assertions.assertEquals("this.instance.field", read("this.instance.field this.instance.anotherField")),

                () -> Assertions.assertEquals("1", read("1")),
                () -> Assertions.assertEquals("1.0", read("1.0")),
                () -> Assertions.assertEquals("1.0D", read("1.0D")),
                () -> Assertions.assertEquals("0x001", read("0x001 call()")),

                () -> Assertions.assertEquals("1+1", read("1 + 1")),
                () -> Assertions.assertEquals("1+1", read("1 + 1 call() + 1")),

                () -> Assertions.assertEquals("(1.0)", read("(1.0)")),
                () -> Assertions.assertEquals("(1.0)+1.0", read("(1.0) + 1.0 call()"))
        );
    }

    private @Nullable String read(String source) {
        Tokens tokens = PARSER.read(PandaLexerUtils.convert(source));

        if (!TokensUtils.isEmpty(tokens)) {
            System.out.println(source + " : " + ((ExpressionTokens) tokens).getSubparser().getName());
        }

        return TokensUtils.asString(tokens);
    }

    static {
        ParserData data = new PandaParserData();
        ExpressionSubparsers subparsers = new ExpressionSubparsers(new ArrayList<>());
        PARSER = new ExpressionParser(null, subparsers);

        ExpressionSubparsersLoader loader = new ExpressionSubparsersLoader();
        ExpressionSubparsers loadedSubparsers = null;

        try {
            loadedSubparsers = loader.load(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        subparsers.merge(loadedSubparsers);
    }
    
}
