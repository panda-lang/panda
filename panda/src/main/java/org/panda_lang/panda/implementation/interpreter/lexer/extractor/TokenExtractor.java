package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenType;
import org.panda_lang.core.interpreter.lexer.suggestion.Separator;
import org.panda_lang.core.util.array.ArrayDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TokenExtractor {

    public static final TokenPatternUnit HOLLOW = new TokenPatternUnit(new TokenType("HOLLOW"), "*");

    private final TokenPattern pattern;
    private final List<TokenHollow> hollows;
    private final Stack<Separator> separators;
    private TokenHollow hollow;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
        this.hollows = new ArrayList<>();
        this.separators = new Stack<>();
        this.hollow = new TokenHollow();
    }

    /*
        method main(String str) { return; }

        method * ( * ) { * }

        hollows:
            : main
            : String str
            : return;

     */
    public boolean extract(TokenReader tokenReader) {
        TokenPatternUnit[] tokenUnits = pattern.getUnits();
        ArrayDistributor<TokenPatternUnit> unitsDistributor = new ArrayDistributor<>(tokenUnits);

        for (int unitIndex = 0; unitIndex < tokenUnits.length; unitIndex++) {
            TokenPatternUnit unit = tokenUnits[unitIndex];
            TokenPatternUnit nextUnit = unitsDistributor.get(unitIndex + 1);

            for (Token nextToken : tokenReader) {
                if (nextToken.equals(unit)) {
                    break;
                }

                if (!unit.isHollow()) {
                    return false;
                }

                if (!unit.equals(nextUnit)) {
                    hollow.addToken(nextToken);
                    break;
                }

                hollows.add(hollow);
                hollow = new TokenHollow();
                break;
            }
        }

        return tokenReader.hasNext();
    }

    public List<TokenHollow> getHollows() {
        return hollows;
    }

}
