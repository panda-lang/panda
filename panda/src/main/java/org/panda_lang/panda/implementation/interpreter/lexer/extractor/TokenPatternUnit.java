package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.EqualableToken;

public class TokenPatternUnit extends EqualableToken {

    private final TokenType tokenType;
    private final String token;

    public TokenPatternUnit(TokenType tokenType, String token) {
        this.tokenType = tokenType;
        this.token = token;
    }

    public boolean isHollow() {
        return TokenExtractor.HOLLOW.getType().equals(tokenType);
    }

    @Override
    public String getTokenValue() {
        return token;
    }

    @Override
    public TokenType getType() {
        return tokenType;
    }

}
