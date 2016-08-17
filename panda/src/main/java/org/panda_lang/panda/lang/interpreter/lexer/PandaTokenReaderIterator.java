package org.panda_lang.panda.lang.interpreter.lexer;

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
        this.index = -1;
    }

    public void reset() {
        this.index = -1;
        this.sourceArrayDistributor.reset();

        if (tokenArrayDistributor == null) {
            tokenArrayDistributor = pandaTokenReader.nextLine(sourceArrayDistributor);
            return;
        }

        this.tokenArrayDistributor.reset();
    }

    @Override
    public Iterator<Token> iterator() {
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

    public int getIndex() {
        return index;
    }

}
