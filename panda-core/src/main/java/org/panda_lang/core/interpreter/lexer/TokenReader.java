package org.panda_lang.core.interpreter.lexer;

import java.util.Iterator;

public interface TokenReader extends Iterable<TokenRepresentation>, Iterator<TokenRepresentation> {

    TokenRepresentation read();

    TokenReaderIterator iterator();

    default void synchronize() {
        iterator().synchronize();
    }

    default TokenRepresentation next() {
        return iterator().next();
    }

    default TokenRepresentation previous() {
        return iterator().previous();
    }

    default boolean hasNext() {
        return iterator().hasNext();
    }

    void setIndex(int index);

    int getIndex();

    TokenizedSource getTokenizedSource();

}
