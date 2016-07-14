package org.panda_lang.core.interpreter.parser;

public class ParserStatus {

    private ParserError parserError;

    public void print(ParserError parserError) {
        System.out.println(parserError.getContent());
    }

    public <T> T throwParserError(ParserError parserError) {
        this.parserError = parserError;

        print(parserError);
        return null;
    }

    public boolean checkStatus() {
        return parserError == null;
    }

}
