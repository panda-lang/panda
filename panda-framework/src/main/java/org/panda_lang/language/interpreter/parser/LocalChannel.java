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

package org.panda_lang.language.interpreter.parser;

import java.util.Map;

/**
 * The communication channel between {@link org.panda_lang.language.interpreter.parser.pipeline.Handler} and parser.
 * In some cases, it may be helpful to reuse some already processed data from handler.
 */
public interface LocalChannel {

    /**
     * Put some unique data in the channel
     *
     * @param identifier the identifier (key) of data
     * @param value the value to store
     * @return the allocated instance
     */
    <T> T allocated(String identifier, T value);

    /**
     * Put some data in the channel and override the previous value in case of duplicate
     *
     * @param identifier the identifier (key) of data
     * @param value the value to store
     * @param <T> type of data
     * @return the allocated instance
     */
    <T> T override(String identifier, T value);

    /**
     * Check if channel contains the given value
     *
     * @param type the type to search for
     * @return true if channel contains the requested data
     */
    boolean contains(Class<?> type);

    /**
     * Check if channel contains the given value
     *
     * @param element the identifier to search for
     * @return true if channel contains the requested data
     */
    boolean contains(String element);

    /**
     * Get data from the channel
     *
     * @param type type of value to get
     * @param <T> the type
     * @return the data
     */
    <T> T get(Class<T> type);

    /**
     * Get data from the channel
     *
     * @param identifier the identifier of value to get
     * @param <T> the type
     * @return the data
     */
    <T> T get(String identifier);

    /**
     * Get data from the channel
     *
     * @param identifier the identifier of value to get
     * @param type the class of the data
     * @param <T> the type
     * @return the data
     */
    <T> T get(String identifier, Class<T> type);

    /**
     * Get data container
     *
     * @return the map that contains channel data
     */
    Map<String, Object> getData();

}
