package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.suggestion.Separator;
import org.panda_lang.core.util.array.ArrayDistributor;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TokenExtractor {

    public static final TokenPatternUnit HOLLOW = new TokenPatternUnit(new TokenType("HOLLOW"), "*");

    private final TokenPattern pattern;
    private final List<TokenHollow> hollows;
    private TokenHollow hollow;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
        this.hollows = new ArrayList<>();
        this.hollow = new TokenHollow();
    }

    public boolean extract(TokenReader tokenReader) {
        return extract(tokenReader.getTokenizedSource());
    }

    public boolean extract(TokenizedSource tokenizedSource) {
        TokenPatternUnit[] units = pattern.getUnits();
        ArrayDistributor<TokenPatternUnit> unitsDistributor = new ArrayDistributor<>(units);

        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);
        Stack<Separator> separators = new Stack<>();

        for (int unitIndex = 0; unitIndex < units.length; unitIndex++) {
            TokenPatternUnit unit = unitsDistributor.get(unitIndex);
            TokenPatternUnit nextUnit = unitsDistributor.get(unitIndex + 1);

            loop:
            if (!unit.isHollow()) {

            }
            else {

            }
        }

        return tokenReader.getIndex() >= tokenizedSource.size();
    }

    public List<TokenHollow> getHollows() {
        return hollows;
    }

}
