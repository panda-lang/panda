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

package org.panda_lang.language.interpreter.parser.pool;

import org.panda_lang.language.interpreter.parser.Parser;

import java.util.Collection;

/**
 * Container of pipelines
 */
public interface ParserPoolService {

    /**
     * Create a new pipeline based on the given component
     *
     * @param component the component to use
     * @param <P> type of parsers represented by pipeline
     * @return a new pipeline
     */
    <P extends Parser> ParserPool<P> computeIfAbsent(Target<P> component);

    /**
     * Check if path contains the given pipeline
     *
     * @param component the identifier of pipeline to search for
     * @return true if pipeline exists in the path, otherwise false
     */
    boolean hasPool(Target<?> component);

    /**
     * Get pipeline using the given component
     *
     * @param component the component to search for
     * @param <P> the type of represented parsers by requested pipeline
     * @return the found pipeline
     */
    <P extends Parser> ParserPool<P> getPool(Target<P> component);

    /**
     * Collect names of pipelines
     *
     * @return the collection of pipelines names
     */
    Collection<String> names();

}
