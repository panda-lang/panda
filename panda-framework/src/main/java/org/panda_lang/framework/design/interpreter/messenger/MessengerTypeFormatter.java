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

package org.panda_lang.framework.design.interpreter.messenger;

/**
 * Utility class to register a couple of placeholders assigned to the same type
 *
 * @param <T> the generic type of formatter
 * @see org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter#register(String, Class, FormatterFunction)
 */
public interface MessengerTypeFormatter<T> {

    /**
     * Register a new placeholder
     *
     * @param placeholder the name of placeholder
     * @param replacementFunction the value to replace with
     * @return formatter instance
     */
    MessengerTypeFormatter<T> register(String placeholder, FormatterFunction<T> replacementFunction);

    /**
     * Type of formatter
     *
     * @return the type
     */
    Class<T> getType();

}
