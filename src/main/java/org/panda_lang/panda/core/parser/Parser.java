package org.panda_lang.panda.core.parser;

import org.panda_lang.panda.core.work.Executable;

public interface Parser {

    Executable parse(ParserInfo parserInfo);

}
