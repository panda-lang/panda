package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;

public interface ParserHandler {

    /**
     * @param tokenReader source
     * @return returns true if parser fits to source
     */
    boolean handle(TokenReader tokenReader);

}
