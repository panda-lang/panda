package org.panda_lang.core.interpreter.parser;

public class ParserStatus {

    private ParserError parserError;

    public void throwParserError(ParserError parserError) {

    }

    public boolean checkStatus() {
        return parserError == null;
    }

}
