package org.panda_lang.core.interpreter.lexer;

import org.panda_lang.core.interpreter.token.Token;

public interface TokenRepresentation {

    int getLine();

    Token getToken();

}
