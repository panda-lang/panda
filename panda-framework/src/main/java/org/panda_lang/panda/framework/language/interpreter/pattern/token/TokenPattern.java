package org.panda_lang.panda.framework.language.interpreter.pattern.token;

public class TokenPattern {

    private final TokenPatternElement patternContent;

    TokenPattern(TokenPatternBuilder builder) {
        this.patternContent = builder.patternContent;
    }

    protected TokenPatternElement getPatternContent() {
        return patternContent;
    }

    public static TokenPatternBuilder builder() {
        return new TokenPatternBuilder();
    }

}
