/*
 * Copyright (c) 2015-2018 Dzikoysk
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

public class ArrayUtils {

    /**
     * Check if the specified array contains null
     *
     * @return the array to search
     */
    public static boolean containsNull(Object[] array) {
        return contains(array, null);
    }

    /**
     * Check if the specified array contains the element
     *
     * @param array the array to search
     * @param element the element to search for
     * @return true if the specified array contains the element, otherwise false
     */
    public static boolean contains(Object[] array, Object element) {
        for (Object arrayElement : array) {
            if (element == null) {
                if (arrayElement == null) {
                    return true;
                }

                continue;
            }

            if (element.equals(arrayElement)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get element of array at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param array the array to process
     * @param index the index of element to get
     * @param <T> type of the array
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified array
     */
    public static <T> @Nullable T get(T[] array, int index) {
        return index > -1 && index < array.length ? array[index] : null;
    }

    /**
     * Return array of the specified elements using varargs parameter
     *
     * @param elements elements in array
     * @param <T> type of the array
     * @return the array of the specified elements
     */
    @SafeVarargs
    public static <T> T[] of(T... elements) {
        return elements;
    }

}
