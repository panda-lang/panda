/*
 * Copyright (c) 2015-2019 Dzikoysk
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

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Message formatter
 */
public interface MessengerFormatter {

    /**
     * Format message using registered placeholders and with provided data
     *
     * @param message the message to format
     * @param context the values to use as data for placeholders
     * @return formatted message
     */
    String format(String message, Collection<Object> context);

    /**
     * Register a new placeholder
     *
     * @param placeholder the name of the placeholder
     * @param requiredData the required type of data to use that placeholder (if null, it means that placeholder does not require provided context)
     * @param replacementFunction the value to replace with
     * @param <T> type of required data
     * @return the instance of formatter
     */
    <T> MessengerFormatter register(String placeholder, @Nullable Class<T> requiredData, FormatterFunction<?> replacementFunction);

    /**
     * Register group of placeholders associated with the specified type
     *
     * @param type the type of the group
     * @param <T> type
     * @return instance of type formatter
     */
    <T> MessengerTypeFormatter<T> getTypeFormatter(Class<T> type);

    /**
     * Clone formatter into a new instance
     *
     * @return the new instance
     */
    MessengerFormatter fork();

    /**
     * Get {@link org.panda_lang.framework.design.interpreter.messenger.Messenger}
     *
     * @return the messenger instance
     */
    Messenger getMessenger();

}
