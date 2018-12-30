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

package org.panda_lang.panda.framework.design.interpreter.pattern.token;

import org.junit.jupiter.api.Test;

class TokenPatternTest {

    @Test
    public void testMethodPattern() {
        TokenPatternTester.test(
                "(method|local|hidden) [static] [<return-type>] <name> `( [<*parameters>] `) `{ <*body> `}",

                "method void anotherEcho() { Console.print(message); }",

                TokenPatternTester.Wildcard.of("return-type", "void"),
                TokenPatternTester.Wildcard.of("name", "anotherEcho"),
                TokenPatternTester.Wildcard.of("*parameters", ""),
                TokenPatternTester.Wildcard.of("*body", "Console.print(message);")
        );
    }

    @Test
    public void testImportPattern() {
        TokenPatternTester.test(
                "import <import:condition token {type:unknown}, token {type:separator}, token {type:operator}>[;]",

                "import panda-lang",

                TokenPatternTester.Wildcard.of("import", "panda-lang")
        );
    }

    @Test
    public void testModulePattern() {
        TokenPatternTester.test(
                "module <module:condition token {type:unknown}, token {type:separator}, token {type:operator}>[;]",

                "module example-test import panda-lang;",

                TokenPatternTester.Wildcard.of("module", "example-test")
        );
    }

    @Test
    public void testMethod() {
        TokenPatternTester.test(
                "<*expression> `( [<*parameters>] `)",

                "a.b().c(d)",

                TokenPatternTester.Wildcard.of("*expression", "a.b"),
                TokenPatternTester.Wildcard.of("*parameters", "")
        );
    }

    @Test
    public void testStaticMethod() {
        TokenPatternTester.test(
                "[<*instance> .] <name> `( <*arguments> `) [;]",

                "Console.print(message3);",

                TokenPatternTester.Wildcard.of("*instance", "Console"),
                TokenPatternTester.Wildcard.of("name", "print"),
                TokenPatternTester.Wildcard.of("*arguments", "message3")
        );
    }

}
