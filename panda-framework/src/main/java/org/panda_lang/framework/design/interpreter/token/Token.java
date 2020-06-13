/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.design.interpreter.token;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.function.Option;

import java.util.Objects;

/**
 * The smallest piece of data used by Panda Framework.
 * Tokens represent elements of {@link org.panda_lang.framework.design.resource.Syntax} with identified meaning.
 */
public interface Token {

    /**
     * Get value of the token
     *
     * @return the value
     */
    String getValue();

    /**
     * Get type of the token represented by {@link org.panda_lang.framework.design.interpreter.token.TokenType}
     *
     * @return the type of the token
     */
    TokenType getType();

    /**
     * Get custom name of the token
     *
     * @return optional that may contain custom name of token
     */
    default Option<String> getName() {
        return Option.none();
    }

    /**
     * Check if the token has custom name
     *
     * @return true if token contains name
     */
    default boolean hasName() {
        return getName().isPresent();
    }

    default boolean equals(@Nullable Token token) {
        return token != null && getType() == token.getType() && Objects.equals(getName(), token.getName()) && Objects.equals(getValue(), token.getValue());
    }

}
