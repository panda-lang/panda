package org.panda_lang.core.interpreter.token.util;

import org.panda_lang.core.interpreter.token.Token;

import java.util.List;

public class TokenUtils {

    public static String get(TokensSet tokensSet, int i) {
        List<Token> tokenList = tokensSet.getTokens();

        if (i >= tokenList.size()) {
            return null;
        }

        Token token = tokenList.get(0);
        return token.getName();
    }

}
