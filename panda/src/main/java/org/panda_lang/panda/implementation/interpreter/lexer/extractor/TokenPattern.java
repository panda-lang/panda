package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

public class TokenPattern {

    private final TokenPatternUnit[] units;

    protected TokenPattern(TokenPatternUnit[] units) {
        this.units = units;
    }

    public TokenExtractor extractor() {
        return new TokenExtractor(this);
    }

    public static TokenPatternBuilder builder() {
        return new TokenPatternBuilder();
    }

}
