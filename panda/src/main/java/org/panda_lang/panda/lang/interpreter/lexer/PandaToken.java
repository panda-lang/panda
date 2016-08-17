package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenType;

public class PandaToken implements Token {

    private final TokenType type;
    private final String token;

    public PandaToken(TokenType tokenType, String token) {
        this.type = tokenType;
        this.token = token;
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
