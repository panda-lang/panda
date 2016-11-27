package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.framework.interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenPatternBuilder {

    private final List<TokenPatternUnit> units;
    private boolean keepOpposites;

    public TokenPatternBuilder() {
        this.units = new ArrayList<>();
        this.keepOpposites = true;
    }

    public TokenPatternBuilder keepOpposites(boolean keepOpposites) {
        this.keepOpposites = keepOpposites;
        return this;
    }

    public TokenPatternBuilder unit(TokenType type, String token) {
        TokenPatternUnit unit = new TokenPatternUnit(type, token);
        units.add(unit);
        return this;
    }

    public TokenPatternBuilder gap() {
        units.add(TokenExtractor.GAP);
        return this;
    }

    public TokenPattern build() {
        TokenPatternUnit[] unitsArray = new TokenPatternUnit[units.size()];
        units.toArray(unitsArray);

        return new TokenPattern(unitsArray, keepOpposites);
    }

}
