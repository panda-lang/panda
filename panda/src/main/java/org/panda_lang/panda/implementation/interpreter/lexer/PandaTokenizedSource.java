package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.token.TokensSet;

public class PandaTokenizedSource implements TokenizedSource {

    private final TokenRepresentation[] tokenizedSource;

    public PandaTokenizedSource(TokensSet tokensSet) {
        this(tokensSet.toArray());
    }

    public PandaTokenizedSource(TokenRepresentation[] representations) {
        this.tokenizedSource = representations;
    }

    @Override
    public TokenRepresentation[] getSource() {
        return tokenizedSource;
    }

}
