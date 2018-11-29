package org.panda_lang.panda.interpreter.parser.implementation.general.expression;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexerUtils;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.DefaultSubparsers;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.PandaExpressionParser;

class ExpressionParserTest {

    @Test
    public void testRead() {
        PandaExpressionParser parser = new PandaExpressionParser(DefaultSubparsers.getDefaultSubparsers());

        Assertions.assertAll(
                () -> Assertions.assertEquals("true", read(parser, "true")),
                () -> Assertions.assertEquals("true", read(parser, "true false"))
        );
    }

    private @Nullable String read(PandaExpressionParser parser, String source) {
        Tokens tokens = parser.read(PandaLexerUtils.convert(source));
        return tokens != null ? tokens.asString() : null;
    }

}
