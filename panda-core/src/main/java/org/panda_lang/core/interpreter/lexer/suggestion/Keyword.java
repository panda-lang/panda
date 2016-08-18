package org.panda_lang.core.interpreter.lexer.suggestion;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenType;

public class Keyword implements Token {

    private final String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getToken() {
        return keyword;
    }

    @Override
    public TokenType getType() {
        return TokenType.KEYWORD;
    }

    @Override
    public String toString() {
        return getType().name().toLowerCase() + ": " + getToken();
    }

}
