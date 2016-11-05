package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.runtime.element.Executable;

public interface Parser {

    Executable parse(ParserInfo parserInfo);

}
