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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListUtils {

    /**
     * Reverse the provided list and return its
     *
     * @param list the list to reverse
     * @param <T> type of the list
     * @return the reversed list
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * Create mutable list from varargs
     *
     * @param elements to add to the array
     * @param <T> type of the list
     * @return created list
     */
    @SafeVarargs
    public static <T> List<T> mutableOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * Get element of list at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param list the list to process
     * @param index the index of element to get
     * @param <T> type of the list
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified list
     */
    public static <T> @Nullable T get(List<T> list, int index) {
        return index > -1 && index < list.size() ? list.get(index) : null;
    }

}
