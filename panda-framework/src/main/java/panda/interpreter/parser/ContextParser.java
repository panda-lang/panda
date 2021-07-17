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

package panda.interpreter.parser;

import org.jetbrains.annotations.NotNull;
import panda.std.Completable;
import panda.std.Option;

/**
 * Represents parsers supported by pipelines
 *
 * @param <T> type of result
 */
public interface ContextParser<T, R> extends Parser, Comparable<ContextParser<?, ?>> {

    double DEFAULT_PRIORITY = 1.0;

    /**
     * Get parser priority
     *
     * @return the priority
     */
    default double priority() {
        return DEFAULT_PRIORITY;
    }

    /**
     * Get targeted pools by this parser
     *
     * @return the targeted pools
     */
    Component<?>[] targets();

    /**
     * Initialize parser
     *
     * @param context the context used to initialize parser
     */
    default void initialize(Context<?> context) { }

    /**
     * Parse context
     *
     * @param context set of information about source and interpretation process
     */
    Option<Completable<R>> parse(Context<? extends T> context);

    @Override
    default int compareTo(@NotNull ContextParser<?, ?> to) {
        return Double.compare(priority(), to.priority());
    }

}
