package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.util.array.ArrayDistributor;
import org.panda_lang.panda.implementation.interpreter.lexer.PandaTokenizedSource;

import java.util.ArrayList;
import java.util.List;

public class TokenExtractor {

    public static final TokenPatternUnit GAP = new TokenPatternUnit(new TokenType("GAP"), "*");

    private final TokenPattern pattern;
    private final List<TokenizedSource> gaps;

    public TokenExtractor(TokenPattern pattern) {
        this.pattern = pattern;
        this.gaps = new ArrayList<>();
    }

    public boolean extract(TokenReader tokenReader) {
        tokenReader.synchronize();

        TokenPatternUnit[] units = pattern.getUnits();
        ArrayDistributor<TokenPatternUnit> unitsDistributor = new ArrayDistributor<>(units);
        TokenExtractorOpposites extractorOpposites = new TokenExtractorOpposites(this);
        TokenizedSource gap = new PandaTokenizedSource();

        for (int unitIndex = 0; unitIndex < units.length; unitIndex++) {
            TokenPatternUnit unit = unitsDistributor.get(unitIndex);
            tokenReader.synchronize();

            for (TokenRepresentation tokenRepresentation : tokenReader) {
                Token token = tokenRepresentation.getToken();

                if (unit.equals(token)) {
                    tokenReader.read();
                    break;
                }

                if (!unit.isGap()) {
                    return false;
                }

                extractorOpposites.report(token);
                gap.addToken(tokenRepresentation);
                tokenReader.read();
                tokenReader.synchronize();

                TokenPatternUnit nextUnit = unitsDistributor.get(unitIndex + 1);

                for (TokenRepresentation gapRepresentation : tokenReader) {
                    Token gapToken = gapRepresentation.getToken();

                    if (!gapToken.equals(nextUnit) || extractorOpposites.isLocked()) {
                        extractorOpposites.report(gapToken);
                        tokenReader.read();
                        tokenReader.synchronize();
                        gap.addToken(gapRepresentation);
                        continue;
                    }

                    break;
                }

                gaps.add(gap);
                gap = new PandaTokenizedSource();
                break;
            }
        }

        return tokenReader.getIndex() + 1 >= tokenReader.getTokenizedSource().size();
    }

    protected TokenPattern getPattern() {
        return pattern;
    }

    public List<TokenizedSource> getGaps() {
        return gaps;
    }

}
