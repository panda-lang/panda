package org.panda_lang.core.interpreter.parser;

public interface ParserStatus {

    void print(ParserError parserError);

    <T> T throwParserError(ParserError parserError);

    boolean checkStatus();

}
