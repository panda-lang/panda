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

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;

import java.security.InvalidParameterException;

public class PandaTokenRepresentation implements TokenRepresentation {

    private final Token token;
    private final int line;

    public PandaTokenRepresentation(Token token, int line) {
        if (token == null) {
            throw new InvalidParameterException("Token cannot be null");
        }

        this.token = token;
        this.line = line;
    }

    @Override
    public int getLine() {
        return line;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public String toString() {
        return token.getTokenValue();
    }

    public static TokenRepresentation of(TokenType type, String value) {
        return of(new PandaToken(type, value));
    }

    public static TokenRepresentation of(Token token) {
        return new PandaTokenRepresentation(token, -1);
    }

}
