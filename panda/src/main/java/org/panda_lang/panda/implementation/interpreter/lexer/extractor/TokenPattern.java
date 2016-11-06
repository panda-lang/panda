package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

public class TokenPattern {

    private final TokenPatternUnit[] units;
    private boolean keepingOpposites;

    protected TokenPattern(TokenPatternUnit[] units, boolean keepOpposites) {
        this.units = units;
        this.keepingOpposites = keepOpposites;
    }

    public TokenExtractor extractor() {
        return new TokenExtractor(this);
    }

    public static TokenPatternBuilder builder() {
        return new TokenPatternBuilder();
    }

    public boolean isKeepingOpposites() {
        return keepingOpposites;
    }

    public TokenPatternUnit[] getUnits() {
        return units;
    }

}
