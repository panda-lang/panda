package org.panda_lang.framework.interpreter.parser;

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
     * @return priority
     */
    int getPriority();

    /**
     * @return associated handler
     */
    ParserHandler getHandler();

    /**
     * @return associated parser
     */
    UnifiedParser getParser();

}
