/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.interpreter.token;

import org.panda_lang.framework.interpreter.source.Location;
import org.panda_lang.utilities.commons.ObjectUtils;
import org.panda_lang.utilities.commons.ValidationUtils;

import java.security.InvalidParameterException;
import java.util.Objects;

public final class PandaTokenInfo implements TokenInfo {

    private final Token token;
    private final Location location;

    public PandaTokenInfo(Token token, Location location) {
        if (token == null) {
            throw new InvalidParameterException("Token cannot be null");
        }

        if (location == null) {
            throw new InvalidParameterException("Location of token cannot be null");
        }

        this.token = ValidationUtils.notNull(token);
        this.location = location;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object to) {
        return ObjectUtils.equals(this, getToken(), to, PandaTokenInfo::getToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getToken());
    }

    @Override
    public String toString() {
        return token.toString();
    }

    /*
    public static TokenInfo of(TokenType type, String value) {
        return of(new PandaToken(type, value));
    }

    public static TokenInfo of(Token token) {
        Source source = new PandaSource("<unknown>", token.getValue());
        Location location = new PandaLocation(source, Location.UNKNOWN_LOCATION, Location.UNKNOWN_LOCATION);
        return new PandaTokenInfo(token, location);
    }
     */

}
