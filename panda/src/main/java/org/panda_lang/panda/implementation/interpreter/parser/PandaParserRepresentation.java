package org.panda_lang.panda.implementation.interpreter.parser;

import org.panda_lang.framework.interpreter.parser.ParserHandler;
import org.panda_lang.framework.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.interpreter.parser.UnifiedParser;

public class PandaParserRepresentation implements ParserRepresentation {

    private final UnifiedParser parser;
    private final ParserHandler handler;
    private final int priority;
    private int usages;

    public PandaParserRepresentation(UnifiedParser parser, ParserHandler parserHandler, int priority) {
        this.parser = parser;
        this.handler = parserHandler;
        this.priority = priority;
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
    public int getPriority() {
        return priority;
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