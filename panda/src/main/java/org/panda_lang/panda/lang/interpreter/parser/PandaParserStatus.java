package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.ParserError;
import org.panda_lang.core.interpreter.parser.ParserStatus;

public class PandaParserStatus implements ParserStatus {

    private ParserError parserError;

    @Override
    public void print(ParserError parserError) {
        System.out.println(parserError.getContent());
    }

    @Override
    public <T> T throwParserError(ParserError parserError) {
        this.parserError = parserError;

        print(parserError);
        return null;
    }

    @Override
    public boolean checkStatus() {
        return parserError == null;
    }

}
