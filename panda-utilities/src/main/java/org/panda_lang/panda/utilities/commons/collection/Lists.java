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

package org.panda_lang.panda.utilities.commons.collection;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Lists {

    /**
     * Returns a view of the portion of the given list
     *
     * @param list the list to use
     * @param startIndex low endpoint (inclusive) of the subList
     * @param <T> type of list
     * @return a view of the specified range within this list
     */
    public static <T> List<T> subList(List<T> list, int startIndex) {
        return list.subList(startIndex, list.size());
    }

    /**
     * Sort similar lists using the same comparator
     *
     * @param comparator the comparator to use
     * @param collections the array of lists to sort
     * @param <T> type of the lists
     */
    @SafeVarargs
    public static <T> void sort(Comparator<T> comparator, List<? extends T>... collections) {
        for (List<? extends T> collection : collections) {
            collection.sort(comparator);
        }
    }

    /**
     * Reverse the provided list and return its
     *
     * @param list the list to reverse
     * @param <T>  type of the list
     * @return the reversed list
     */
    public static <T> List<T> reverse(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * Split list by the given delimiter
     *
     * @param list the list to split
     * @param delimiter the object to use as delimiter
     * @param <T> type of list
     * @return the array of lists
     */
    public static <T> List<T>[] split(List<T> list, T delimiter) {
        List<List<T>> elements = new ArrayList<>();
        int previousIndex = -1;

        for (int index = 0; index < list.size(); index++) {
            if (!list.get(index).equals(delimiter)) {
                continue;
            }

            elements.add(list.subList(++previousIndex, index));
            previousIndex = index;
        }

        if (previousIndex > -1) {
            elements.add(subList(list, ++previousIndex));
        }

        if (elements.isEmpty()) {
            elements.add(list);
        }

        //noinspection unchecked
        return elements.toArray(new List[0]);
    }

    /**
     * Create mutable list from varargs
     *
     * @param elements to add to the array
     * @param <T>      type of the list
     * @return created list
     */
    @SafeVarargs
    public static <T> List<T> mutableOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * Get element of list at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param list  the list to process
     * @param index the index of element to get
     * @param <T>   type of the list
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified list
     */
    public static <T> @Nullable T get(List<T> list, int index) {
        return index > -1 && index < list.size() ? list.get(index) : null;
    }

}
