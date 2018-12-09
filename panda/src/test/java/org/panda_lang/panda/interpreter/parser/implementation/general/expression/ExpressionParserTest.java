package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.TokensUtils;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers.DefaultSubparsers;

class ExpressionParserTest {

    private static final ExpressionParser PARSER = new ExpressionParser(DefaultSubparsers.getDefaultSubparsers());

    @Test
    public void testRead() {
        Assertions.assertAll(
                () -> Assertions.assertEquals("true", read("true")),
                () -> Assertions.assertEquals("true", read("true false")),

                () -> Assertions.assertEquals("this.call(a,b)", read("this.call(a,b)")),
                () -> Assertions.assertEquals("this.get().call(a,b)", read("this.get().call(a,b) this.call(a,b)")),

                () -> Assertions.assertEquals("newObject(){}", read("new Object(){}")),
                () -> Assertions.assertEquals("newObject(){}.toString()", read("new Object(){}.toString() call()")),

                () -> Assertions.assertEquals("this.instance", read("this.instance")),
                () -> Assertions.assertEquals("this.instance.field", read("this.instance.field this.instance.anotherField")),

                () -> Assertions.assertEquals("1", read("1")),
                () -> Assertions.assertEquals("1.0", read("1.0")),
                () -> Assertions.assertEquals("0x001", read("0x001 call()")),

                () -> Assertions.assertEquals("1+1", read("1 + 1")),
                () -> Assertions.assertEquals("1+1", read("1 + 1 call() + 1")),

                () -> Assertions.assertEquals("(1.0)", read("(1.0)")),
                () -> Assertions.assertEquals("(1.0)+1.0", read("(1.0) + 1.0 call()"))
        );
    }

    private @Nullable String read(String source) {
        return TokensUtils.asString(PARSER.read(PandaLexerUtils.convert(source)));
    }

}
