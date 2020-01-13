/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.framework.design.interpreter.parser.pipeline;

/**
 * The communication channel between {@link Handler} and parser.
 * In some cases, it may be helpful to reuse some already processed data from handler.
 */
public interface Channel {

    /**
     * Put some data in the channel
     *
     * @param identifier the identifier (key) of data
     * @param value the value to add
     * @return the channel instance
     */
    Channel put(String identifier, Object value);

    /**
     * Get data from the channel
     *
     * @param identifier the identifier of value to get
     * @param type the class of the data
     * @param <T> the type
     * @return the data
     */
    <T> T get(String identifier, Class<T> type);

}
