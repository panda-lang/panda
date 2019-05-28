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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class ArrayUtils {

    private ArrayUtils() { }

    public static <T> @Nullable T findIn(T[] array, Predicate<T> condition) {
        for (T element : array) {
            if (condition.test(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Convert array of elements to list, but skip empty (null) values
     *
     * @param elements the array to convert
     * @param <T>      the type
     * @return the result list
     */
    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        List<T> list = new ArrayList<>(elements.length);

        for (T element : elements) {
            if (element == null) {
                continue;
            }

            list.add(element);
        }

        return list;
    }

    /**
     * Merge several arrays
     *
     * @param arrays arrays to merge
     * @return array of merged arrays
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T[] mergeArrays(T[]... arrays) {
        int size = 0;
        Class<?> type = null;

        for (T[] array : arrays) {
            size += array.length;
            type = array.getClass().getComponentType();
        }

        T[] mergedArray = (T[]) Array.newInstance(type, size);
        int index = 0;

        for (T[] array : arrays) {
            for (T element : array) {
                mergedArray[index++] = element;
            }
        }

        return mergedArray;
    }

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
     * @param array   the array to search
     * @param element the element to search for
     * @return true if the specified array contains the element, otherwise false
     */
    public static boolean contains(Object[] array, Object element) {
        for (Object arrayElement : array) {
            if (Objects.equals(arrayElement, element)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the specified array is null or empty
     *
     * @param array the array to check
     * @return true if array is null or empty
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Get element of array at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param array the array to process
     * @param index the index of element to get
     * @param <T>   type of the array
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified array
     */
    public static <T> @Nullable T get(T[] array, int index) {
        return index > -1 && index < array.length ? array[index] : null;
    }

    /**
     * Get array class for the specified type
     *
     * @param clazz type of array
     * @return array of type
     */
    public static Class<?> getArrayClass(Class<?> clazz) {
        return Array.newInstance(clazz, 0).getClass();
    }

    /**
     * Get dimensional array for the specified type
     *
     * @param type       the type of the array
     * @param dimensions the amount of dimensions
     * @return the class of the dimensional array
     */
    public static Class<?> getDimensionalArrayType(Class<?> type, int dimensions) {
        if (dimensions == 0) {
            throw new IllegalArgumentException("Cannot get dimensional array for 0 dimensions");
        }

        if (dimensions == 1) {
            return type;
        }

        return Array.newInstance(type, new int[dimensions - 1]).getClass();
    }

    /**
     * Return array of the specified elements using varargs parameter
     *
     * @param elements elements in array
     * @param <T>      type of the array
     * @return the array of the specified elements
     */
    @SafeVarargs
    public static <T> T[] of(T... elements) {
        return elements;
    }

}
