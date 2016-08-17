package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.parser.lexer.Token;
import org.panda_lang.core.interpreter.parser.lexer.TokenReader;
import org.panda_lang.core.interpreter.parser.lexer.TokenizedSource;
import org.panda_lang.core.interpreter.parser.util.ArrayDistributor;

public class PandaTokenReader implements TokenReader {

    private final TokenizedSource tokenizedSource;
    private ArrayDistributor<Token[]> sourceArrayDistributor;
    private ArrayDistributor<Token> tokenArrayDistributor;
    private int index;

    public PandaTokenReader(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
        this.sourceArrayDistributor = new ArrayDistributor<>(tokenizedSource.getTokenizedSource());
        this.tokenArrayDistributor = nextLine();
        this.index = -1;
    }

    @Override
    public Token read() {
        if (index >= tokenizedSource.size()) {
            return null;
        }

        if (tokenArrayDistributor == null) {
            return null;
        }

        Token token = tokenArrayDistributor.next();

        if (token == null) {
            tokenArrayDistributor = nextLine();
            return read();
        }

        ++index;
        return token;
    }

    protected ArrayDistributor<Token> nextLine() {
        Token[] tokens = sourceArrayDistributor.next();

        if (tokens == null) {
            return null;
        }

        return new ArrayDistributor<>(tokens);
    }

    @Override
    public boolean hasNext() {
        return index + 1 < tokenizedSource.size();
    }

    @Override
    public int getLineIndex() {
        return tokenArrayDistributor.getIndex();
    }

    @Override
    public int getLine() {
        return sourceArrayDistributor.getIndex();
    }

    @Override
    public int getIndex() {
        return index;
    }

    public TokenizedSource getTokenizedSource() {
        return tokenizedSource;
    }

}
