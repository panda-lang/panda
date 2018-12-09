package org.panda_lang.panda.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;

public class TokensUtils {

    public static boolean contains(Tokens source, Token... tokens) {
        for (Token token : tokens) {
            if (source.contains(token)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isEmpty(@Nullable Tokens tokens) {
        return tokens == null || tokens.isEmpty();
    }

    public static String asString(@Nullable Tokens tokens) {
        return tokens != null ? tokens.asString() : null;
    }

}
