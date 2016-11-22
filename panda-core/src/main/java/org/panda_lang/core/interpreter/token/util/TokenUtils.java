package org.panda_lang.core.interpreter.token.util;

import org.panda_lang.core.interpreter.lexer.TokenRepresentation;
import org.panda_lang.core.interpreter.token.Token;
import org.panda_lang.core.interpreter.token.TokensSet;

import java.util.List;

public class TokenUtils {

    public static String extractToken(TokensSet tokensSet, int i) {
        List<TokenRepresentation> tokenList = tokensSet.getTokensRepresentations();

        if (i >= tokenList.size()) {
            return null;
        }

        TokenRepresentation tokenRepresentation = tokenList.get(0);
        Token token = tokenRepresentation.getToken();

        return token.getName();
    }

}
