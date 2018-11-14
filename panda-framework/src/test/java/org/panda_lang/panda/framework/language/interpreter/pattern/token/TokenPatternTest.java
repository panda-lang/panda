package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.lexer.PandaLexer;
import org.panda_lang.panda.framework.language.interpreter.pattern.lexical.elements.LexicalPatternElement;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenExtractorResult;
import org.panda_lang.panda.framework.language.interpreter.source.PandaSource;
import org.panda_lang.panda.framework.language.resource.PandaSyntax;

class TokenPatternTest {

    private static final String CONTENT = "method void test(15, ()25) { Console.print('test') }";
    private static final Tokens SOURCE = new PandaLexer(PandaSyntax.getInstance(), new PandaSource("Test", CONTENT)).convert();

    @Test
    public void testTokenPattern() {
        TokenPattern pattern = TokenPattern.builder()
                .compile("(method|hidden|local) [static] <return-type> <name> `(<*parameters>`) `{ <*body> `}[;]")
                .build();

        LexicalPatternElement content = pattern.getPatternContent();
        Assertions.assertNotNull(content);

        TokenExtractorResult result = pattern.extract(SOURCE);
        Assertions.assertNotNull(result);

        if (result.hasErrorMessage()) {
            System.out.println("Error message: " + result.getErrorMessage());
        }

        Assertions.assertTrue(result.isMatched());
        Assertions.assertNotNull(result.getWildcards());
        Assertions.assertEquals(4, result.getWildcards().size());

        Assertions.assertEquals("void", result.getWildcards().get("return-type").asString());
        Assertions.assertEquals("test", result.getWildcards().get("name").asString());
        Assertions.assertEquals("15,()25", result.getWildcards().get("*parameters").asString());
        Assertions.assertEquals("Console.print(test)", result.getWildcards().get("*body").asString());
    }

}
