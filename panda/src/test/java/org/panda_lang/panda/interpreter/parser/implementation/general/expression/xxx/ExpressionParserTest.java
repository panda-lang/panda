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

package org.panda_lang.panda.interpreter.parser.implementation.general.expression.xxx;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.SnippetUtils;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionParserOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsers;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSubparsersLoaderOld;
import org.panda_lang.panda.framework.design.resource.parsers.expression.xxx.ExpressionSnippet;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserData;

class ExpressionParserTest {

    private static final ExpressionParserOld PARSER = new ExpressionParserOld(null, new ExpressionSubparsers());

    @BeforeAll
    public static void prepareParser() throws Exception {
        ParserData data = new PandaParserData();
        data.setComponent(PandaComponents.EXPRESSION, PARSER);

        ExpressionSubparsers loadedSubparsers = new ExpressionSubparsersLoaderOld().load(data);
        PARSER.getSubparsers().merge(loadedSubparsers);
    }

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
        Snippet snippet = PARSER.read(PandaLexerUtils.convert(source));

        if (!SnippetUtils.isEmpty(snippet)) {
            System.out.println(source + " : " + ((ExpressionSnippet) snippet).getSubparser().getName());
        }

        return SnippetUtils.asString(snippet);
    }

}
