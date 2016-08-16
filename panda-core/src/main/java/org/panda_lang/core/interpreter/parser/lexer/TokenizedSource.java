package org.panda_lang.core.interpreter.parser.lexer;

public interface TokenizedSource {

    Token[] getLine(int lineNumber);

    Token[][] getTokenizedSource();

}
