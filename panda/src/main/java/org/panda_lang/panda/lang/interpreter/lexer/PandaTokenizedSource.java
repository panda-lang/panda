package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.core.interpreter.parser.lexer.TokenizedSource;

public class PandaTokenizedSource implements TokenizedSource {

    private final Token[][] tokenizedSource;

    public PandaTokenizedSource(Token[][] tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
    }

    public Token[] getLine(int lineNumber) {
        return lineNumber < tokenizedSource.length ? tokenizedSource[lineNumber] : null;
    }

    @Override
    public Token[][] getTokenizedSource() {
        return tokenizedSource;
    }

}
