package org.panda_lang.core.interpreter.token.util;

import org.panda_lang.core.interpreter.token.Token;

import java.util.ArrayList;
import java.util.List;

public class TokensSet {

    private final List<Token> tokens;

    public TokensSet() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        tokens.add(token);
    }

    public List<Token> getTokens() {
        return tokens;
    }

}
