package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenType;

public class TokenPatternUnit implements Token {

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
    public String getToken() {
        return token;
    }

    @Override
    public TokenType getType() {
        return tokenType;
    }

}
