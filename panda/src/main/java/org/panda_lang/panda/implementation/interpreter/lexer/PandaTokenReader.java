package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.framework.interpreter.lexer.TokenReader;
import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.util.array.ArrayDistributor;

public class PandaTokenReader implements TokenReader {

    private final TokenizedSource tokenizedSource;
    private ArrayDistributor<TokenRepresentation> representationsDistributor;
    private PandaTokenReaderIterator iterator;

    public PandaTokenReader(TokenReader tokenReader) {
        this(tokenReader.getTokenizedSource());
        this.setIndex(tokenReader.getIndex());
    }

    public PandaTokenReader(TokenizedSource tokenizedSource) {
        this.tokenizedSource = tokenizedSource;
        this.representationsDistributor = new ArrayDistributor<>(tokenizedSource.toArray());
        this.iterator = new PandaTokenReaderIterator(this);
    }

    @Override
    public TokenRepresentation read() {
        return representationsDistributor.next();
    }

    @Override
    public PandaTokenReaderIterator iterator() {
        return iterator;
    }

    @Override
    public void setIndex(int index) {
        representationsDistributor.setIndex(index);
        synchronize();
    }

    @Override
    public int getIndex() {
        return representationsDistributor.getIndex();
    }

    @Override
    public TokenizedSource getTokenizedSource() {
        return tokenizedSource;
    }

}
