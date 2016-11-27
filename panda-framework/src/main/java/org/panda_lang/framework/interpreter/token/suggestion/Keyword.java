package org.panda_lang.framework.interpreter.token.suggestion;

import org.panda_lang.framework.interpreter.token.TokenType;
import org.panda_lang.framework.interpreter.token.util.EqualableToken;

public class Keyword extends EqualableToken {

    private final String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getTokenValue() {
        return keyword;
    }

    @Override
    public TokenType getType() {
        return TokenType.KEYWORD;
    }

    @Override
    public String toString() {
        return getType().getTypeName().toLowerCase() + ": " + getTokenValue();
    }

}
