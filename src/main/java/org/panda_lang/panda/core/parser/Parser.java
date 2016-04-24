package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.statement.Executable;

public interface Parser {

    Executable parse(ParserInfo parserInfo);

}
