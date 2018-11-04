package org.panda_lang.panda.framework.language.interpreter.pattern.token;

import org.junit.jupiter.api.Test;

class TokenPatternTest {

    @Test
    public void testTokenPattern() {
        TokenPattern pattern = TokenPattern.builder()
                .compile("(method|hidden|local) [static] <return-type> <name>(<parameters>) {{ <body> }}[;]")
                .build();

        TokenPatternElement content = pattern.getPatternContent();
    }

}
