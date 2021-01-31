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

package org.panda_lang.utilities.commons;

import org.jetbrains.annotations.Nullable;

public final class ValidationUtils {

    private ValidationUtils() { }

    /**
     * Require not null value or throw exception
     *
     * @param value the value to validate
     * @param <T> type of value
     * @return non null value
     */
    public static <T> T notNull(@Nullable T value) {
        return notNull(value, "Object cannot be null");
    }

    /**
     * Require not null value or throw exception with the given message
     *
     * @param value the value to validate
     * @param message the message to throw if value is null
     * @param <T> type of value
     * @return non null value
     */
    public static <T> T notNull(@Nullable T value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

}
