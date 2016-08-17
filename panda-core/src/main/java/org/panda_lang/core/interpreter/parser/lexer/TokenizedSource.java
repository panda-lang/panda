package org.panda_lang.core.interpreter.parser.lexer;

public interface TokenizedSource {

    int size();

    Token[] getLine(int lineNumber);

    Token[][] getTokenizedSource();

}
