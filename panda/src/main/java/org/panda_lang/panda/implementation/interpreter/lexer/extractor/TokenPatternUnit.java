package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.lexer.TokenType;

public class TokenPatternUnit {

    private final TokenType tokenType;
    private final String token;

    public TokenPatternUnit(TokenType tokenType, String token) {
        this.tokenType = tokenType;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

}
