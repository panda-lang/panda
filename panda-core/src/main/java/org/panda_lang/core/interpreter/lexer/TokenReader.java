package org.panda_lang.core.interpreter.lexer;

import java.util.Iterator;

public interface TokenReader extends Iterator<Token>, Iterable<Token> {

    Token read();

    void reset();

    TokenizedSource getTokenizedSource();

    int getLineIndex();

    int getLine();

    int getNextIndex();

    int getIndex();

}
