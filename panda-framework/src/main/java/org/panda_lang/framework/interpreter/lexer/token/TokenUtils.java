/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.framework.interpreter.lexer.token;

import org.panda_lang.framework.interpreter.lexer.TokenRepresentation;
import org.panda_lang.framework.interpreter.lexer.TokenizedSource;

public class TokenUtils {

    public static String extractToken(TokenizedSource tokenizedSource, int i) {
        if (i >= tokenizedSource.size()) {
            return null;
        }

        Token token = tokenizedSource.getToken(0);
        return token.getName();
    }

    public static boolean equals(TokenRepresentation tokenRepresentation, Token token) {
        return equals(tokenRepresentation, token.getType(), token.getTokenValue());
    }

    public static boolean equals(TokenRepresentation tokenRepresentation, TokenType tokenType, String tokenValue) {
        if (tokenRepresentation == null) {
            return false;
        }

        Token token = tokenRepresentation.getToken();
        return token.getType() == tokenType && token.getTokenValue().equals(tokenValue);
    }

}
