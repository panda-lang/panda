package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;

public interface ParserContext {

    /**
     * @param tokenReader set the main token reader
     */
    void setTokenReader(TokenReader tokenReader);

    /**
     * @return the main token reader for parsed text
     */
    TokenReader getTokenReader();

    /**
     * @return raw source
     */
    String getSource();

}
