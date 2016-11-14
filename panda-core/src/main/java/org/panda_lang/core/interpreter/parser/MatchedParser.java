package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.runtime.element.Executable;

public interface MatchedParser extends Parser {

    /**
     * @param parserInfo set of information about source and interpretation process
     * @return parsed executable
     */
    Executable parse(ParserInfo parserInfo);

}
