package org.panda_lang.framework.interpreter.lexer;

import org.panda_lang.framework.interpreter.token.Token;

import java.util.List;

public interface TokenizedSource {

    default int size() {
        return getTokensRepresentations().size();
    }

    default void addToken(TokenRepresentation tokenRepresentation) {
        getTokensRepresentations().add(tokenRepresentation);
    }

    default TokenRepresentation get(int id) {
        if (id >= size() || id < 0) {
            return null;
        }

        return getTokensRepresentations().get(id);
    }

    default Token getToken(int id) {
        TokenRepresentation tokenRepresentation = get(id);

        if (tokenRepresentation == null) {
            return null;
        }

        return tokenRepresentation.getToken();
    }

    List<TokenRepresentation> getTokensRepresentations();

    TokenRepresentation[] toArray();

}
