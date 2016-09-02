package org.panda_lang.core.interpreter.lexer;

import java.util.Iterator;

public interface TokenReaderIterator extends Iterator<TokenRepresentation> {

    void synchronize();

    void setIndex(int index);

    int getIndex();

}
