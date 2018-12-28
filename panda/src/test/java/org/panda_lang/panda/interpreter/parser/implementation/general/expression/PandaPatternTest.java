package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.PatternContentBuilder;

class PandaPatternTest {

    private static final String METHOD = "[<instance:reader expression exclude method, field> .] <name> `( [<*arguments>] `) [;]";
    private static final String VARIABLE = "([[mutable] [nullable] <type>] <name:condition token {type:unknown}>|<name:reader expression include field>) [= <assignation:reader expression>][;]";

    @Test
    public void testDeclaration() {
        Assertions.assertFalse(PandaPatternTester.build("<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]", "String init nullable Object req = null").isMatched());
    }

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

                "this.variable = this testField",

                PandaPatternTester.Wildcard.of("name", "this.variable"),
                PandaPatternTester.Wildcard.of("assignation", "this")
        );
    }

    @Test
    public void testAssignationWithConstructor() {
        PandaPatternTester.test(
                VARIABLE,

                "varFoo = new Foo();",

                PandaPatternTester.Wildcard.of("name", "varFoo"),
                PandaPatternTester.Wildcard.of("assignation", "newFoo()")
        );
    }

    @Test
    public void testVariableNameAssignation() {
        PandaPatternTester.test(
                VARIABLE,

                "testField = this;",

                PandaPatternTester.Wildcard.of("name", "testField"),
                PandaPatternTester.Wildcard.of("assignation", "this")
        );
    }

    @Test
    public void testVariableWithContent() {
        PandaPatternTester.test(
                VARIABLE,

                "this.testField = this testField = this this.echo(String.valueOf(i))",

                PandaPatternTester.Wildcard.of("name", "this.testField"),
                PandaPatternTester.Wildcard.of("assignation", "this")
        );
    }

    @Test
    public void testMutableNullableVariableDeclaration() {
        PandaPatternTester.test(
                VARIABLE,

                "mutable nullable String x;",

                PandaPatternTester.Wildcard.of("type", "String"),
                PandaPatternTester.Wildcard.of("name", "x")
        );
    }

    @Test
    public void testNestedVariants() {
        PandaPatternTester.test(
                "((if:if|else if) `( <*condition> `)|else)",

                "if (!flag)",

                PandaPatternTester.Wildcard.of("*condition", "!flag")
        );
    }

    @Test
    public void testNested() {
        PandaPatternTester.test(
                "foreach `( <*content> `)",

                "foreach(String var : list)",

                PandaPatternTester.Wildcard.of("*content", "Stringvar:list")
        );
    }

    @Test
    public void testField() {
        PandaPatternTester.test(
                PatternContentBuilder.create()
                        .element("<visibility>")
                        .optional("static", "static")
                        .optional("mutable", "mutable")
                        .optional("nullable", "nullable")
                        .element("<type> <name:condition token {type:unknown}>")
                        .optional("= <assignation:reader expression>")
                        .optional(";")
                        .build(),

                "hidden Double i = 1.0D; hidden mutable Test testField; ",

                PandaPatternTester.Wildcard.of("visibility", "hidden"),
                PandaPatternTester.Wildcard.of("type", "Double"),
                PandaPatternTester.Wildcard.of("name", "i"),
                PandaPatternTester.Wildcard.of("assignation", "1.0D")
        );
    }

}
