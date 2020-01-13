/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.pattern.descriptive;

import org.junit.jupiter.api.Test;

class DescriptivePatternTest {

    @Test
    public void testMethodPattern() {
        DescriptivePatternTester.test(
                "(method|local|hidden) [static] [<return-type>] <name> `( [<*parameters>] `) `{ <*body> `}",

                "method void anotherEcho() { Console.print(message); }",

                DescriptivePatternTester.Wildcard.of("return-type", "void"),
                DescriptivePatternTester.Wildcard.of("name", "anotherEcho"),
                DescriptivePatternTester.Wildcard.of("*body", "Console.print(message);")
        );
    }

    @Test
    public void testImportPattern() {
        DescriptivePatternTester.test(
                "import <import:condition token {type:unknown}, token {type:separator}, token {type:operator}>[;]",

                "import panda-lang",

                DescriptivePatternTester.Wildcard.of("import", "panda-lang")
        );
    }

    @Test
    public void testModulePattern() {
        DescriptivePatternTester.test(
                "module <module:condition token {type:unknown}, token {type:separator}, token {type:operator}>[;]",

                "module example-test import panda-lang;",

                DescriptivePatternTester.Wildcard.of("module", "example-test")
        );
    }

    @Test
    public void testMethod() {
        DescriptivePatternTester.test(
                "<*expression> `( [<*parameters>] `)",

                "a.b().c(d)",

                DescriptivePatternTester.Wildcard.of("*expression", "a.b")
        );
    }

    @Test
    public void testStaticMethod() {
        DescriptivePatternTester.test(
                "[<*instance> .] <name> `( <*arguments> `) [;]",

                "Console.print(message3);",

                DescriptivePatternTester.Wildcard.of("*instance", "Console"),
                DescriptivePatternTester.Wildcard.of("name", "print"),
                DescriptivePatternTester.Wildcard.of("*arguments", "message3")
        );
    }

    @Test
    public void testField() {
        DescriptivePatternTester.test(
                "(p:public|l:local|h:hidden) s:[static] m:[mut] n:[nil] <type:reader type> <name:condition token {type:unknown}> [= <assignation:reader expression>][;]",

                "hidden mut Test testField",

                DescriptivePatternTester.Wildcard.of("type", "Test"),
                DescriptivePatternTester.Wildcard.of("name", "testField")
        );
    }

}
