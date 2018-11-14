/*
 * Copyright (c) 2015-2018 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.language.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class TokenUtils {

    public static boolean isNumber(Token token) {
        return StringUtils.isNumber(token.getTokenValue());
    }

    public static boolean equals(TokenRepresentation tokenRepresentation, Token token) {
        return equals(tokenRepresentation, token.getType(), token.getTokenValue());
    }

    public static boolean equals(TokenRepresentation tokenRepresentation, TokenType tokenType, String tokenValue) {
        return tokenRepresentation != null && equals(tokenRepresentation.getToken(), tokenType, tokenValue);
    }

    public static boolean equals(Token token, Token another) {
        return equals(another, token.getType(), token.getTokenValue());
    }

    public static boolean equals(Token anotherToken, TokenType tokenType, String tokenValue) {
        return anotherToken.getType() == tokenType && anotherToken.getTokenValue().equals(tokenValue);
    }

    public static @Nullable String extractToken(Tokens tokens, int i) {
        if (i >= tokens.size()) {
            return null;
        }

        Token token = tokens.getToken(0);

        if (token == null) {
            return null;
        }

        return token.getName();
    }

    public static boolean contains(Tokens source, Token token) {
        return contains(source, token.getType(), token.getTokenValue());
    }

    public static boolean contains(Tokens source, TokenType type, String value) {
        for (TokenRepresentation representation : source.getTokensRepresentations()) {
            if (equals(representation, type, value)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isTypeOf(TokenRepresentation representation, TokenType type) {
        return type.equals(representation.getToken().getType());
    }

}
