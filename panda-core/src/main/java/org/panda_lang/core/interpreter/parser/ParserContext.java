package org.panda_lang.core.interpreter.parser;

import org.panda_lang.core.interpreter.lexer.TokenReader;
import org.panda_lang.core.interpreter.lexer.TokenizedSource;

public interface ParserContext {

    TokenReader getTokenReader();

    void setTokenReader(TokenReader tokenReader);

    TokenizedSource getTokenizedSource();

    String getSource();

}
