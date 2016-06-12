package org.panda_lang.panda.core.parser;

public class ParserInfo<T> {

    private T parser;
    private ParserStatus parserStatus;

    public ParserStatus getParserStatus() {
        return parserStatus;
    }

}
