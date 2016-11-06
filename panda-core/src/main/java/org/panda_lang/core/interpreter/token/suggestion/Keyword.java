package org.panda_lang.core.interpreter.token.suggestion;

import org.panda_lang.core.interpreter.token.TokenType;
import org.panda_lang.core.interpreter.token.util.EqualableToken;

public class Keyword extends EqualableToken {

    private final String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getToken();
    }

    @Override
    public String getToken() {
        return keyword;
    }

    @Override
    public TokenType getType() {
        return TokenType.KEYWORD;
    }

}
