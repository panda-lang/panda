package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenPatternBuilder {

    private final List<TokenPatternUnit> units;

    public TokenPatternBuilder() {
        units = new ArrayList<>();
    }

    public TokenPatternBuilder unit(TokenType type, String token) {
        TokenPatternUnit unit = new TokenPatternUnit(type, token);
        units.add(unit);
        return this;
    }

    public TokenPatternBuilder hollow() {
        units.add(TokenExtractor.HOLLOW);
        return this;
    }

    public TokenPattern build() {
        TokenPatternUnit[] unitsArray = new TokenPatternUnit[units.size()];
        units.toArray(unitsArray);

        return new TokenPattern(unitsArray);
    }

}
