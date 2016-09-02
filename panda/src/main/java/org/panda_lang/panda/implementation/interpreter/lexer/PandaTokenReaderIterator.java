package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenReaderIterator;
import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.util.array.ArrayDistributor;

public class PandaTokenReaderIterator implements TokenReaderIterator {

    private final TokenReader tokenReader;
    private ArrayDistributor<TokenRepresentation> representationsDistributor;
    private int index;

    public PandaTokenReaderIterator(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
        this.representationsDistributor = new ArrayDistributor<>(tokenReader.getTokenizedSource().getSource());
        this.index = -1;
    }

    @Override
    public void synchronize() {
        setIndex(tokenReader.getIndex());
        representationsDistributor.setIndex(index);
    }

    @Override
    public TokenRepresentation next() {
        return representationsDistributor.next();
    }

    @Override
    public boolean hasNext() {
        return index + 1 < tokenReader.getTokenizedSource().size();
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {
        return index;
    }

}
