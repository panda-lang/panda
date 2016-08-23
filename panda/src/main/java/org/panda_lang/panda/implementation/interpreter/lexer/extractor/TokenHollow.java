package org.panda_lang.panda.implementation.interpreter.lexer.extractor;

import org.panda_lang.core.interpreter.lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class TokenHollow {

    private final List<Token> tokens;

    public TokenHollow() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }

}
