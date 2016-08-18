package org.panda_lang.panda.lang.interpreter.parser;

import org.panda_lang.core.interpreter.parser.Parser;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserRepresentation;

public class PandaParserRepresentation implements ParserRepresentation {

    private final Parser parser;
    private final ParserHandler handler;
    private int usages;

    public PandaParserRepresentation(Parser parser, ParserHandler parserHandler) {
        this.parser = parser;
        this.handler = parserHandler;
    }

    @Override
    public void increaseUsages() {
        ++usages;
    }

    @Override
    public int getUsages() {
        return usages;
    }

    @Override
    public ParserHandler getHandler() {
        return handler;
    }

    @Override
    public Parser getParser() {
        return parser;
    }

}