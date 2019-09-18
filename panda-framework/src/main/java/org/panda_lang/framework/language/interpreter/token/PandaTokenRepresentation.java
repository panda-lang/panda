/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.token;

import org.panda_lang.framework.design.interpreter.source.Source;
import org.panda_lang.framework.design.interpreter.token.Token;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.framework.design.interpreter.token.TokenType;
import org.panda_lang.framework.language.interpreter.source.PandaSource;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.security.InvalidParameterException;
import java.util.Objects;

public class PandaTokenRepresentation implements TokenRepresentation {

    private final Token token;
    private final SourceLocation location;

    public PandaTokenRepresentation(Token token, SourceLocation location) {
        if (token == null) {
            throw new InvalidParameterException("Token cannot be null");
        }

        if (location == null) {
            throw new InvalidParameterException("Location of token cannot be null");
        }

        this.token = token;
        this.location = location;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public SourceLocation getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object to) {
        return ObjectUtils.equals(this, getToken(), to, PandaTokenRepresentation::getToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken());
    }

    @Override
    public String toString() {
        return token.getValue();
    }

    public static TokenRepresentation of(TokenType type, String value) {
        return of(new PandaToken(type, value));
    }

    public static TokenRepresentation of(Token token) {
        Source source = new PandaSource("<unknown>", token.getValue());
        SourceLocation location = new PandaSourceLocation(source, SourceLocation.UNKNOWN_LOCATION, SourceLocation.UNKNOWN_LOCATION);
        return new PandaTokenRepresentation(token, location);
    }

}
