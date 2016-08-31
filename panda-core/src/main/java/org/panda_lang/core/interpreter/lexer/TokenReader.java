package org.panda_lang.core.interpreter.lexer;

import org.panda_lang.core.interpreter.token.Token;

import java.util.Iterator;

public interface TokenReader extends Iterator<Token>, Iterable<Token> {

    Token read();

    void synchronize();

    TokenizedSource getTokenizedSource();

    int getIteratorLineIndex();

    int getIteratorLine();

    int getIteratorIndex();

    int getLineIndex();

    int getLine();

    int getIndex();

}
