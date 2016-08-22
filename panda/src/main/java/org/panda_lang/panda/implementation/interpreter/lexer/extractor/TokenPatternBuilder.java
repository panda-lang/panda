package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import java.util.ArrayList;
import java.util.List;

public class TokenPatternBuilder {

    private final List<TokenPatternUnit> units;

    public TokenPatternBuilder() {
        units = new ArrayList<>();
    }

    public TokenPattern build() {
        TokenPatternUnit[] unitsArray = new TokenPatternUnit[units.size()];
        units.toArray(unitsArray);

        return new TokenPattern(unitsArray);
    }

}
