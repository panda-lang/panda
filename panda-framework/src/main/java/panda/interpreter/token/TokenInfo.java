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

package panda.interpreter.token;

import panda.interpreter.source.Localizable;
import panda.interpreter.source.Location;

import panda.std.Option;

import java.util.Collections;

/**
 * Wrapper for {@link Token} that contains details about location of token in source
 */
public interface TokenInfo extends Token, Snippetable, Localizable {

    /**
     * Compare wrapped token with another token, utility method
     *
     * @param token the token to compare with
     * @return true if tokens are equal
     */
    default boolean contentEquals(Token token) {
        return getToken().equals(token);
    }

    /**
     * Get the wrapped token
     *
     * @return the original token
     */
    Token getToken();

    /**
     * Get location of token
     *
     * @return the location
     */
    Location getLocation();

    /**
     * Get token using {@link #getToken()} method and cast it to the requested type
     *
     * @param type the class type
     * @param <T> the type to cast
     * @return casted value, otherwise {@link java.lang.ClassCastException}
     */
    @SuppressWarnings("unchecked")
    default <T extends Token> T toToken(Class<T> type) {
        return (T) getToken();
    }

    /**
     * Get token using {@link #getToken()} method and cast it to the requested type
     *
     * @param <T> the type to cast
     * @return casted value, otherwise {@link java.lang.ClassCastException}
     */
    @SuppressWarnings("unchecked")
    default <T extends Token> T toToken() {
        return (T) getToken();
    }

    @Override
    default Snippet toSnippet() {
        return PandaSnippet.ofImmutable(Collections.singletonList(this));
    }

    @Override
    default Location toLocation() {
        return getLocation();
    }

    @Override
    default boolean hasName() {
        return getToken().hasName();
    }

    @Override
    default Option<String> getName() {
        return getToken().getName();
    }

    @Override
    default String getValue() {
        return getToken().getValue();
    }

    @Override
    default TokenType getType() {
        return getToken().getType();
    }

}
