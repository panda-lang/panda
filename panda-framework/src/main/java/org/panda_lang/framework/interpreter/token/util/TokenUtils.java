package org.panda_lang.framework.interpreter.token.util;

import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;
import org.panda_lang.framework.interpreter.token.Token;
import org.panda_lang.framework.interpreter.token.TokenType;

public class TokenUtils {

    public static String extractToken(TokenizedSource tokenizedSource, int i) {
        if (i >= tokenizedSource.size()) {
            return null;
        }

        Token token = tokenizedSource.getToken(0);
        return token.getName();
    }

    public static boolean equals(TokenRepresentation tokenRepresentation, TokenType tokenType, String tokenValue) {
        Token token = tokenRepresentation.getToken();
        return token.getType() == tokenType && token.getTokenValue().equals(tokenValue);
    }

}
