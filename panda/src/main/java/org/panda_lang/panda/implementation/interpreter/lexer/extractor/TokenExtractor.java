package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.util.array.ArrayDistributor;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenReader;

import java.util.ArrayList;
import java.util.List;

public class TokenExtractor {

    public static final TokenPatternUnit HOLLOW = new TokenPatternUnit(new TokenType("HOLLOW"), "*");

    private final TokenPattern pattern;
    private final List<TokenHollow> hollows;
    private final TokenExtractorOpposites extractorOpposites;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
        this.hollows = new ArrayList<>();
        this.extractorOpposites = new TokenExtractorOpposites(this);
    }

    public boolean extract(TokenReader tokenReader) {
        return extract(tokenReader.getTokenizedSource());
    }

    public boolean extract(TokenizedSource tokenizedSource) {
        TokenPatternUnit[] units = pattern.getUnits();
        ArrayDistributor<TokenPatternUnit> unitsDistributor = new ArrayDistributor<>(units);

        TokenReader tokenReader = new PandaTokenReader(tokenizedSource);
        TokenHollow hollow = new TokenHollow();

        for (int unitIndex = 0; unitIndex < units.length; unitIndex++) {
            TokenPatternUnit unit = unitsDistributor.get(unitIndex);
            tokenReader.synchronize();

            for (TokenRepresentation representation : tokenReader) {
                Token token = representation.getToken();

                if (unit.equals(token)) {
                    tokenReader.read();
                    break;
                }

                if (!unit.isHollow()) {
                    return false;
                }

                TokenPatternUnit nextUnit = unitsDistributor.get(unitIndex + 1);

                if (!token.equals(nextUnit) || extractorOpposites.isLocked()) {
                    extractorOpposites.report(token);

                    tokenReader.read();
                    hollow.addToken(token);

                    continue;
                }

                hollows.add(hollow);
                hollow = new TokenHollow();
                break;
            }
        }

        return tokenReader.getIndex() + 1 >= tokenizedSource.size();
    }

    protected TokenPattern getPattern() {
        return pattern;
    }

    public List<TokenHollow> getHollows() {
        return hollows;
    }

}
