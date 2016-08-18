package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;

public interface ParserContext {

    void setTokenReader(TokenReader tokenReader);

    TokenReader getTokenReader();

    TokenizedSource getTokenizedSource();

    String getSource();

}
