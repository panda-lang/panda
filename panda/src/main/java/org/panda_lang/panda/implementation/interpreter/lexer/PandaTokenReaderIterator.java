package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenReaderIterator;
import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.util.array.ArrayDistributor;

public class PandaTokenReaderIterator implements TokenReaderIterator {

    private final TokenReader tokenReader;
    private ArrayDistributor<TokenRepresentation> representationsDistributor;

    public PandaTokenReaderIterator(TokenReader tokenReader) {
        this.tokenReader = tokenReader;
        this.representationsDistributor = new ArrayDistributor<>(tokenReader.getTokenizedSource().getSource());
    }

    @Override
    public void synchronize() {
        setIndex(tokenReader.getIndex());
    }

    @Override
    public TokenRepresentation next() {
        return representationsDistributor.next();
    }

    @Override
    public TokenRepresentation previous() {
        return representationsDistributor.previous();
    }

    @Override
    public boolean hasNext() {
        return getIndex() + 1 < tokenReader.getTokenizedSource().size();
    }

    @Override
    public void setIndex(int index) {
        representationsDistributor.setIndex(index);
    }

    @Override
    public int getIndex() {
        return representationsDistributor.getIndex();
    }

}
