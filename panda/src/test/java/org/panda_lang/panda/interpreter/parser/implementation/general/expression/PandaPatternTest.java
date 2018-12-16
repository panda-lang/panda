package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PandaPatternTest {

    private static final String METHOD = "[<instance:reader expression exclude method, field> .] <name> `( [<*arguments>] `) [;]";
    private static final String VARIABLE = "([<type>] <name:condition token {type:unknown}>|<name:reader expression include field>) [= <assignation:reader expression>][;]";

    @Test
    public void testMethod() {
        PandaPatternTester.test(
                METHOD,

                "Console.print('var');",

                PandaPatternTester.Wildcard.of("instance", "Console"),
                PandaPatternTester.Wildcard.of("name", "print"),
                PandaPatternTester.Wildcard.of("*arguments", "var")
        );
    }

    @Test
    public void testConstructor() {
        Assertions.assertFalse(PandaPatternTester.build(METHOD, "Test test = new Test('Constructor');").isMatched());
    }

    @Test
    public void testFieldAsVariable() {
        Assertions.assertFalse(PandaPatternTester.build(
                "[<type>] <name:condition token {type:unknown}> [= <assignation:reader expression>][;]",
                "this.testField = this;"
        ).isMatched());
    }

    @Test
    public void testVariable() {
        PandaPatternTester.test(
                VARIABLE,

                "String variable = 'Test';",

                PandaPatternTester.Wildcard.of("type", "String"),
                PandaPatternTester.Wildcard.of("name", "variable"),
                PandaPatternTester.Wildcard.of("assignation", "Test")
        );
    }

    @Test
    public void testFieldBasedVariable() {
        PandaPatternTester.test(
                VARIABLE,

                "this.variable = 'Test';",

                PandaPatternTester.Wildcard.of("name", "this.variable"),
                PandaPatternTester.Wildcard.of("assignation", "Test")
        );
    }

}
