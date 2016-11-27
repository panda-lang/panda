package org.panda_lang.framework.interpreter.lexer;

import org.panda_lang.framework.interpreter.token.Token;

public interface TokenRepresentation {

    int getLine();

    Token getToken();

}
