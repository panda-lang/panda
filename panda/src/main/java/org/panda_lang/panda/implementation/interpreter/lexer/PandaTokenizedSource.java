package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PandaTokenizedSource implements TokenizedSource {

    private final List<TokenRepresentation> tokens;

    public PandaTokenizedSource() {
        this.tokens = new ArrayList<>();
    }

    public PandaTokenizedSource(TokenRepresentation[] tokenRepresentations) {
        this.tokens = Arrays.asList(tokenRepresentations);
    }

    @Override
    public List<TokenRepresentation> getTokensRepresentations() {
        return tokens;
    }

    @Override
    public TokenRepresentation[] toArray() {
        TokenRepresentation[] array = new TokenRepresentation[tokens.size()];
        return tokens.toArray(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

}
