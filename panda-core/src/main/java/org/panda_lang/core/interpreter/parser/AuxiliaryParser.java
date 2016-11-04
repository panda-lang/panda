package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.token.util.TokensSet;
import org.panda_lang.core.work.element.Executable;

public interface AuxiliaryParser {

    Executable parse(ParserInfo parserInfo, TokensSet tokensSet);

}
