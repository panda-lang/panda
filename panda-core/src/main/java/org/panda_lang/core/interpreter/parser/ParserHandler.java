package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;

public interface ParserHandler {

    boolean handle(TokenReader tokenReader);

}
