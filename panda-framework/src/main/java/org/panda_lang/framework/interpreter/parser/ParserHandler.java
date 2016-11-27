package org.panda_lang.framework.interpreter.parser;

import org.panda_lang.framework.interpreter.lexer.TokenReader;

public interface ParserHandler {

    /**
     * @param tokenReader source
     * @return returns true if parser fits to source
     */
    boolean handle(TokenReader tokenReader);

}
