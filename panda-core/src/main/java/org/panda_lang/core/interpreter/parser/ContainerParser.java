package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.structure.Statement;

public interface ContainerParser extends Parser {

    /**
     * @param parserInfo set of information about source and interpretation process
     * @return parsed executable
     */
    Statement parse(ParserInfo parserInfo);

}
