package org.panda_lang.core.interpreter.parser;

public interface ParserRepresentation {

    /**
     * Add 1 to number of use. It's used to optimization process of parsing.
     */
    void increaseUsages();

    /**
     * @return amount of usages
     */
    int getUsages();

    /**
     * @return associated handler
     */
    ParserHandler getHandler();

    /**
     * @return associated parser
     */
    ContainerParser getParser();

}
