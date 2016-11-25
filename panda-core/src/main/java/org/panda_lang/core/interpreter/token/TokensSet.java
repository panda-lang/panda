package org.panda_lang.core.interpreter.token;

import org.panda_lang.core.interpreter.lexer.TokenRepresentation;

import java.util.ArrayList;
import java.util.List;

public class TokensSet {

    protected final List<TokenRepresentation> tokens;

    public TokensSet() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(TokenRepresentation token) {
        tokens.add(token);
    }

    public Token getToken(int id) {
        TokenRepresentation tokenRepresentation = get(id);

        if (tokenRepresentation == null) {
            return null;
        }

        return tokenRepresentation.getToken();
    }

    public TokenRepresentation get(int id) {
        if (id < 0 || id >= tokens.size()) {
            return null;
        }

        return tokens.get(id);
    }

    public TokenRepresentation[] toArray() {
        TokenRepresentation[] array = new TokenRepresentation[tokens.size()];
        return tokens.toArray(array);
    }

    public List<TokenRepresentation> getTokensRepresentations() {
        return tokens;
    }

}
