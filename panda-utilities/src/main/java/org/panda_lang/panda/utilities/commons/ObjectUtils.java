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

package org.panda_lang.panda.utilities.commons;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ObjectUtils {

    /**
     * Check if object is not null
     *
     * @param object object to check
     * @return true if object is not null
     */
    public static boolean isNotNull(@Nullable Object object) {
        return object != null;
    }

    /**
     * Check if all values are null
     *
     * @param objects array to check
     * @return true if all values are null
     */
    public static boolean areNull(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }

        return objects.length > 0;
    }

    /**
     * Check if the value is one of the expected
     *
     * @param value value to check
     * @param expected expected values
     * @return true if expected values contains the specified value
     */
    public static boolean equalsOneOf(Object value, Object... expected) {
        for (Object expectedValue : expected) {
            if (Objects.equals(value, expectedValue)) {
                return true;
            }
        }

        return false;
    }

}
