package org.panda_lang.core.interpreter.lexer;

public interface TokenizedSource {

    int size();

    Token[] getLine(int lineNumber);

    Token[][] getSource();

}
