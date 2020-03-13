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

package org.panda_lang.utilities.commons;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.function.ThrowingConsumer;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ArrayUtils {

    private ArrayUtils() { }

    /**
     * {@link java.lang.Iterable#forEach(java.util.function.Consumer)} for arrays
     *
     * @param array the array to iterate
     * @param consumer array values consumer
     * @param <T> type of array
     */
    public static <T> void forEach(T[] array, Consumer<T> consumer) {
        for (T element : array) {
            consumer.accept(element);
        }
    }

    /**
     * {@link java.lang.Iterable#forEach(java.util.function.Consumer)} for arrays using {@link org.panda_lang.utilities.commons.function.ThrowingConsumer}
     *
     * @param array the array to iterate
     * @param consumer array values consumer with exceptions support
     * @param <T> type of array
     * @param <E> type of exception
     * @throws E if consumer will throw exception
     */
    public static <T, E extends Exception> void forEachThrowing(T[] array, ThrowingConsumer<T, E> consumer) throws E {
        for (T element : array) {
            consumer.accept(element);
        }
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
        if (isEmpty(arrays)) {
            throw new IllegalArgumentException("Merge arrays requires at least one array as argument");
        }

        return mergeArrays(length -> (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length), arrays);
    }

    /**
     * Merge arrays into one array
     *
     * @param arrayFunction array instance supplier
     * @param arrays arrays to merge
     * @param <T> type of arrays
     * @return the merged array
     */
    @SafeVarargs
    public static <T> T[] mergeArrays(Function<Integer, T[]> arrayFunction, T[]... arrays) {
        T[] merged = arrayFunction.apply(length(arrays));
        int index = 0;

        for (T[] array : arrays) {
            System.arraycopy(array, 0, merged, index, array.length);
            index += array.length;
        }

        return merged;
    }

    /**
     * Create a new array with the given element at the first position and copy the rest of array
     *
     * @param firstElement the element to add at the first position
     * @param array the array to copy
     * @param arrayFunction array instance supplier with array size as an argument
     * @param <T> type of array
     * @return the merged array
     */
    public static <T> T[] merge(T firstElement, T[] array, Function<Integer, T[]> arrayFunction) {
        T[] merged = arrayFunction.apply(array.length + 1);
        merged[0] = firstElement;
        System.arraycopy(array, 0, merged, 1, array.length);
        return merged;
    }

    /**
     * Find value in array using the predicate condition
     *
     * @param array the array to search in
     * @param condition the condition
     * @param <T> type of array
     * @return the found element or null
     */
    public static <T> Optional<T> findIn(T[] array, Predicate<T> condition) {
        for (T element : array) {
            if (condition.test(element)) {
                return Optional.ofNullable(element);
            }
        }

        return Optional.empty();
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
     * Get length of all given arrays
     *
     * @param arrays the arrays to sum up
     * @param <T> type of arrays
     * @return arrays length
     */
    @SafeVarargs
    public static <T> int length(T[]... arrays) {
        int size = 0;

        for (T[] array : arrays) {
            size += array.length;
        }

        return size;
    }

    /**
     * Check if the given object is array
     *
     * @param object the object to check
     * @return true if object is not null and its class represents array, otherwise false
     */
    public static boolean isArray(@Nullable Object object) {
        return object != null && object.getClass().isArray();
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
     * Get base class of any array, e.g. Integer from Integer[][][][][]
     *
     * @param arrayClass the array class
     * @return the base type
     */
    public static Class<?> getBaseClass(Class<?> arrayClass) {
        Class<?> currentClass = arrayClass;

        while (currentClass.isArray()) {
            currentClass = arrayClass.getComponentType();
        }

        return currentClass;
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
     * Get index of element that pass the condition
     *
     * @param array the array to search in
     * @param condition the condition to test elements
     * @param <T> type of array
     * @return index of element or -1 if element was not found
     */
    public static <T> int getIndex(T[] array, Predicate<T> condition) {
        for (int i = 0; i < array.length; i++) {
            if (condition.test(array[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Get element of array at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param array the array to process
     * @param index the index of element to get
     * @param <T>   type of the array
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified array
     */
    public static <T> Optional<T> get(T[] array, int index) {
        return index > -1 && index < array.length ? Optional.ofNullable(array[index]) : Optional.empty();
    }

    /**
     * Get element of array at the given position without risk of {@link java.lang.ArrayIndexOutOfBoundsException}
     *
     * @param array the array to process
     * @param index the index of element to get
     * @param defaultValue the default value to return when there is no such an index in the array
     * @param <T>   type of the array
     * @return the element at the index position, null if the index is less than 0 or greater than the size of the specified array
     */
    public static <T> T get(T[] array, int index, T defaultValue) {
        return index > -1 && index < array.length ? array[index] : defaultValue;
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
