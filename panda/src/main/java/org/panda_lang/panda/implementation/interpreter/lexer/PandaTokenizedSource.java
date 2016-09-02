package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;

public class PandaTokenizedSource implements TokenizedSource {

    private final TokenRepresentation[] tokenizedSource;

    public PandaTokenizedSource(TokenRepresentation[] tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
    }

    @Override
    public TokenRepresentation[] getSource() {
        return tokenizedSource;
    }

}
