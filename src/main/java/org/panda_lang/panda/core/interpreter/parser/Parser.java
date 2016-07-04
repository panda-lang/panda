package org.panda_lang.panda.core.interpreter.parser;

import org.panda_lang.panda.core.work.executable.Executable;

public interface Parser {

    Executable parse(ParserInfo parserInfo);

}
