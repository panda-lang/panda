package org.panda_lang.core.interpreter.parser.lexer;

import java.util.Iterator;

public interface TokenReader extends Iterator<Token>, Iterable<Token> {

    Token read();

    default Token next() {
        return read();
    }

    default Iterator<Token> iterator() {
        return this;
    }

    int getLineIndex();

    int getLine();

    int getIndex();

}
