package org.panda_lang.framework.interpreter.lexer;

import java.util.Iterator;

public interface TokenReaderIterator extends Iterator<TokenRepresentation> {

    TokenRepresentation previous();

    void synchronize();

    void setIndex(int index);

    int getIndex();

}
