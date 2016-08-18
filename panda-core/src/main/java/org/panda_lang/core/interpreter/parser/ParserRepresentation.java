package org.panda_lang.core.interpreter.parser;

public interface ParserRepresentation {

    void increaseUsages();

    int getUsages();

    ParserHandler getHandler();

    Parser getParser();

}
