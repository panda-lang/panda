package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.junit.jupiter.api.Test;

class TokenPatternTest {

    @Test
    public void testMethodPattern() {
        TokenPatternTester.test(
                "(method|local|hidden) [static] [<return-type>] <name> `( <*parameters> `) `{ <*body> `}",

                "method void anotherEcho(String message) { Console.print(message); }",

                TokenPatternTester.Wildcard.of("return-type", "void"),
                TokenPatternTester.Wildcard.of("name", "anotherEcho"),
                TokenPatternTester.Wildcard.of("*parameters", "Stringmessage"),
                TokenPatternTester.Wildcard.of("*body", "Console.print(message);")
        );
    }

    @Test
    public void testClassPattern() {

    }

}
