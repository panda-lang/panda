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

package org.panda_lang.language.interpreter.pattern;

import org.panda_lang.language.interpreter.token.Token;
import org.panda_lang.utilities.commons.function.Option;

/**
 * Simple interface for pattern results that allows to get matched values
 */
public interface Mappings extends PatternResult{

    default boolean has(Token token) {
        return has(token.getValue());
    }

    default boolean has(String id) {
        return get(id).isDefined();
    }

    default <T> Option<T> get(String id, Class<T> type) {
        return get(id);
    }

    /**
     * Get value using the given name
     *
     * @param id the name to search for
     * @param <T> type of result
     * @return a found result
     */
    <T> Option<T> get(String id);

}
