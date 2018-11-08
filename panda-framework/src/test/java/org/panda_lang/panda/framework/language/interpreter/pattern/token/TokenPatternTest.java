package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.TokenizedSource;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

class TokenPatternTest {

    private static final String CONTENT = "method void test(15, 25) { Console.print('test') }";
    private static final TokenizedSource SOURCE = new PandaLexer(PandaSyntax.getInstance(), new PandaSource("Test", CONTENT)).convert();

    @Test
    public void testTokenPattern() {
        TokenPattern pattern = TokenPattern.builder()
                .compile("(method|hidden|local) [static] <return-type> <name>(<parameters>) \\{ <body> \\}[;]")
                .build();

        LexicalPatternElement content = pattern.getPatternContent();
        Assertions.assertNotNull(content);

        TokenExtractorResult result = pattern.extract(SOURCE);
        Assertions.assertNotNull(result);

        //Assertions.assertTrue(result.isMatched());
    }

}
