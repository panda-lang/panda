package org.panda_lang.panda.lexer;

import org.junit.Assert;
import org.junit.Test;
import org.panda_lang.panda.framework.implementation.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.syntax.PandaSyntax;

public class LexerTest {

    @Test
    public void testKeywordsInUnknown() {
        PandaLexer lexer = new PandaLexer(PandaSyntax.getInstance(), "this.intValue()");
        TokenizedSource source = lexer.convert();

        Assert.assertEquals("this . intValue ( )", source.toString());
    }

}
