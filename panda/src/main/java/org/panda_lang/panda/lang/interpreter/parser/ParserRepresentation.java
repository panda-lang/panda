package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserHandler;

public class ParserRepresentation {

    private final Parser parser;
    private final ParserHandler handler;
    private int usages;

    public ParserRepresentation(Parser parser, ParserHandler parserHandler) {
        this.parser = parser;
        this.handler = parserHandler;
    }

    public void increaseUsages() {
        ++usages;
    }

    public int getUsages() {
        return usages;
    }

    public ParserHandler getHandler() {
        return handler;
    }

    public Parser getParser() {
        return parser;
    }

}
