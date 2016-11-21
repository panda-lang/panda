package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.core.interpreter.parser.UnifiedParser;
import org.panda_lang.core.interpreter.parser.ParserHandler;
import org.panda_lang.core.interpreter.parser.ParserRepresentation;

public class PandaParserRepresentation implements ParserRepresentation {

    private final UnifiedParser parser;
    private final ParserHandler handler;
    private int usages;

    public PandaParserRepresentation(UnifiedParser parser, ParserHandler parserHandler) {
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
    public UnifiedParser getParser() {
        return parser;
    }

}