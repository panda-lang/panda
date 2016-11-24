package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.structure.Statement;

public interface UnifiedParser extends Parser {

    /**
     * @param parserInfo set of information about source and interpretation process
     * @return parsed executable
     */
    Statement parse(ParserInfo parserInfo);

}
