package org.panda_lang.panda.implementation.interpreter.lexer;

import org.panda_lang.core.interpreter.lexer.Token;
import org.panda_lang.core.util.array.ArrayDistributor;

import java.util.Iterator;

public class PandaTokenReaderIterator implements Iterator<Token>, Iterable<Token>  {

    private final PandaTokenReader pandaTokenReader;
    private ArrayDistributor<Token[]> sourceArrayDistributor;
    private ArrayDistributor<Token> tokenArrayDistributor;
    private int index;

    public PandaTokenReaderIterator(PandaTokenReader pandaTokenReader) {
        this.pandaTokenReader = pandaTokenReader;
        this.sourceArrayDistributor = new ArrayDistributor<>(pandaTokenReader.getTokenizedSource().getSource());
        this.tokenArrayDistributor = pandaTokenReader.nextLine(sourceArrayDistributor);
        this.index = -1;
    }

    public void synchronize() {
        this.index = pandaTokenReader.getIndex();
        this.sourceArrayDistributor.setIndex(pandaTokenReader.getLine());

        this.tokenArrayDistributor = new ArrayDistributor<>(sourceArrayDistributor.current());
        this.tokenArrayDistributor.setIndex(pandaTokenReader.getLineIndex());
    }

    @Override
    public PandaTokenReaderIterator iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return index + 1 < pandaTokenReader.getTokenizedSource().size();
    }

    @Override
    public Token next() {
        if (index >= pandaTokenReader.getTokenizedSource().size()) {
            return null;
        }

        if (tokenArrayDistributor == null) {
            return null;
        }

        Token token = tokenArrayDistributor.next();

        if (token == null) {
            tokenArrayDistributor = pandaTokenReader.nextLine(sourceArrayDistributor);
            return next();
        }

        ++index;
        return token;
    }

    public int getLineIndex() {
        return tokenArrayDistributor != null ? tokenArrayDistributor.getIndex() : 0;
    }

    public int getLine() {
        return sourceArrayDistributor.getIndex();
    }

    public int getIndex() {
        return index;
    }

}
