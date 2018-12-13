package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

class PandaPatternTest {

    //@Test
    public void testMethod() {
        PandaPatternTester.test(
                "[<instance:reader expression> .] <name> `( <*arguments> `) [;]",

                "Console.print('var');",

                PandaPatternTester.Wildcard.of("instance", "Console"),
                PandaPatternTester.Wildcard.of("name", "print"),
                PandaPatternTester.Wildcard.of("*arguments", "'var'")
        );
    }

}
