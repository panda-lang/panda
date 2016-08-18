package org.panda_lang.panda.lang.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;
import org.panda_lang.core.util.array.ArrayDistributor;

public class PandaTokenReader implements TokenReader {

    private final TokenizedSource tokenizedSource;
    private ArrayDistributor<Token[]> sourceArrayDistributor;
    private ArrayDistributor<Token> tokenArrayDistributor;
    private PandaTokenReaderIterator iterator;
    private int index;

    public PandaTokenReader(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
        this.sourceArrayDistributor = new ArrayDistributor<>(tokenizedSource.getSource());
        this.tokenArrayDistributor = nextLine(sourceArrayDistributor);
        this.iterator = new PandaTokenReaderIterator(this);
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
            tokenArrayDistributor = nextLine(sourceArrayDistributor);
            return next();
        }

        ++index;
        return token;
    }

    protected ArrayDistributor<Token> nextLine(ArrayDistributor<Token[]> sourceArrayDistributor) {
        Token[] tokens = sourceArrayDistributor.next();

        if (tokens == null) {
            return null;
        }

        return new ArrayDistributor<>(tokens);
    }

    @Override
    public Token next() {
        return iterator.next();
    }

    @Override
    public void synchronize() {
        iterator.synchronize();
    }

    @Override
    public PandaTokenReaderIterator iterator() {
        return iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public TokenizedSource getTokenizedSource() {
        return tokenizedSource;
    }

    @Override
    public int getIteratorLineIndex() {
        return iterator.getLineIndex();
    }

    @Override
    public int getIteratorLine() {
        return iterator.getLine();
    }

    @Override
    public int getIteratorIndex() {
        return iterator.getIndex();
    }

    @Override
    public int getLineIndex() {
        return tokenArrayDistributor != null ? tokenArrayDistributor.getIndex() : 0;
    }

    @Override
    public int getLine() {
        return sourceArrayDistributor.getIndex();
    }

    @Override
    public int getIndex() {
        return index;
    }

}
