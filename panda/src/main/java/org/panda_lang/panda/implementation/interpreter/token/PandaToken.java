package org.panda_lang.panda.implementation.interpreter.token;

import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.interpreter.token.util.EqualableToken;

public class PandaToken extends EqualableToken {

    private final TokenType type;
    private final String token;

    public PandaToken(TokenType tokenType, String token) {
        this.type = tokenType;
        this.token = token;
    }

    @Override
    public String getTokenValue() {
        return token;
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getTokenValue();
    }

}
