package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers.DefaultSubparsers;

class ExpressionParserTest {

    @Test
    public void testRead() {
        ExpressionParser parser = new ExpressionParser(DefaultSubparsers.getDefaultSubparsers());

        Assertions.assertAll(
                () -> Assertions.assertEquals("true", read(parser, "true")),
                () -> Assertions.assertEquals("true", read(parser, "true false")),

                () -> Assertions.assertEquals("this.call(a,b)", read(parser, "this.call(a,b)")),
                () -> Assertions.assertEquals("this.get().call(a,b)", read(parser, "this.get().call(a,b) this.call(a,b)"))
                // () -> Assertions.assertEquals("newObject(){}.toString()", read(parser, "newObject(){}.toString()"))
        );
    }

    private @Nullable String read(ExpressionParser parser, String source) {
        Tokens tokens = parser.read(PandaLexerUtils.convert(source));
        return tokens != null ? tokens.asString() : null;
    }

}
