package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.parser.util.ParserError;

public class ParserStatus {

    private ParserError parserError;

    public void throwParserError(ParserError parserError) {

    }

    public boolean checkStatus() {
        return parserError == null;
    }

}
