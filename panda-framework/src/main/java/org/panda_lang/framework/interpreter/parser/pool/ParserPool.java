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

package org.panda_lang.framework.interpreter.parser.pool;

import org.panda_lang.framework.interpreter.parser.ContextParser;

public interface ParserPool<C> {

    /**
     * Register the specified parser to
     *
     * @param parser specified parser representation which will be registered in the pipeline
     */
    void register(ContextParser<C, ? extends Object> parser);

    /**
     * @return a collection of registered parser
     */
    Iterable<? extends ContextParser<C, ?>> getParsers();

    default PoolParser<C> toParser() {
        return new PoolParser<>(this);
    }

    /**
     * Get name of pipeline
     *
     * @return the name
     */
    String getName();

}
