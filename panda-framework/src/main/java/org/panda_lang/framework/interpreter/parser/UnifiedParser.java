package org.panda_lang.framework.interpreter.parser;

import org.panda_lang.framework.structure.Statement;

public interface UnifiedParser extends Parser {

    /**
     * @param parserInfo set of information about source and interpretation process
     * @return parsed executable
     */
    Statement parse(ParserInfo parserInfo);

}
