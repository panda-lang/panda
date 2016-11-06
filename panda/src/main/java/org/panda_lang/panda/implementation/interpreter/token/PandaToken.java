package org.panda_lang.panda.implementation.interpreter.token;

import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.EqualableToken;

public class PandaToken extends EqualableToken {

    private final TokenType type;
    private final String token;

    public PandaToken(TokenType tokenType, String token) {
        this.type = tokenType;
        this.token = token;
    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getToken();
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public TokenType getType() {
        return type;
    }

}
