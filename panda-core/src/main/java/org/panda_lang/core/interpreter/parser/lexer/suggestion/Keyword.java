package org.panda_lang.core.interpreter.parser.lexer.suggestion;

import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.core.interpreter.parser.lexer.TokenType;

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

}
