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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

final class ArrayUtilsTest {

    private static final String[] EMPTY_ARRAY = {};
    private static final String[] ARRAY = { "a", "b", "c" };
    private static final String[] ARRAY_WITH_NULL = { "a", "b", "c", null };

    @Test
    void containsNull() {
        assertTrue(ArrayUtils.containsNull(ARRAY_WITH_NULL));
        assertFalse(ArrayUtils.containsNull(ARRAY));
        assertFalse(ArrayUtils.containsNull(EMPTY_ARRAY));
    }

    @Test
    void contains() {
        assertTrue(ArrayUtils.contains(ARRAY, "b"));
        assertFalse(ArrayUtils.contains(EMPTY_ARRAY, "d"));
        assertFalse(ArrayUtils.contains(ARRAY_WITH_NULL, "d"));
    }

    @Test
    void mergeArrays() {
        String[] merged = {
                "a", "b", "c",
                "a", "b", "c", null
        };

        assertArrayEquals(merged, ArrayUtils.merge(ARRAY, ARRAY_WITH_NULL));
        assertArrayEquals(merged, ArrayUtils.merge(String[]::new, ARRAY, ARRAY_WITH_NULL));
    }

    @Test
    void isEmpty() {
        assertTrue(ArrayUtils.isEmpty(EMPTY_ARRAY));
        assertFalse(ArrayUtils.isEmpty(ARRAY));
    }

    @Test
    void getArrayClass() {
        assertEquals(String[].class, ArrayUtils.getArrayClass(String.class));
    }

    @Test
    void getDimensionalArrayType() {
        assertEquals(String[].class, ArrayUtils.getDimensionalArrayType(String.class, 2));
    }

    @Test
    void of() {
        assertArrayEquals(new String[] { "a", "b" }, ArrayUtils.of("a", "b"));
    }

    @Test
    void get() {
        assertTrue(ArrayUtils.get(ARRAY, 0).isPresent());
        assertTrue(ArrayUtils.get(ARRAY, 2).isPresent());

        assertFalse(ArrayUtils.get(ARRAY, -1).isPresent());
        assertFalse(ArrayUtils.get(ARRAY, 3).isPresent());
    }
    
    @Test
    void getFirst() {
        assertTrue(ArrayUtils.getFirst(EMPTY_ARRAY).isEmpty());
        assertEquals("a", ArrayUtils.getFirst(ARRAY).get());
    }

    @Test
    void getLast() {
        assertTrue(ArrayUtils.getLast(EMPTY_ARRAY).isEmpty());
        assertEquals("c", ArrayUtils.getLast(ARRAY).get());
    }

    @Test
    void dimensionalArrayClass() {
        assertEquals(Object[][].class, ArrayUtils.getDimensionalArrayType(Object.class, 3));
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.getDimensionalArrayType(Object.class, 0));
    }

    @Test
    void getWithDefaultValue() {
        assertEquals("a", ArrayUtils.get(EMPTY_ARRAY, Integer.MAX_VALUE, "a"));
    }

    @Test
    void forEach() {
        ArrayUtils.forEach(ARRAY, element -> {
            assertTrue(ArrayUtils.contains(ARRAY, element));
        });
    }

    @Test
    void forEachThrowing() {
        ArrayUtils.forEachThrowing(ARRAY, element -> {
            assertTrue(ArrayUtils.contains(ARRAY, element));
        });
    }

    @Test
    void findIn() {
        assertTrue(ArrayUtils.findIn(ARRAY_WITH_NULL, Objects::nonNull).isPresent());
    }

    @Test
    void isArray() {
        //noinspection UnnecessaryLocalVariable
        Object someObject = ARRAY;
        Object anotherObject = "content";

        assertTrue(ArrayUtils.isArray(someObject));
        assertFalse(ArrayUtils.isArray(anotherObject));
    }

    @Test
    void getIndex() {
        assertEquals(1, ArrayUtils.getIndex(ARRAY, element -> element.equals("b")));
    }

    @Test
    void merge() {
        String[] merged = ArrayUtils.merge("-a", ARRAY, String[]::new);
        assertArrayEquals(new String[] { "-a", "a", "b", "c" }, merged);
    }

    @Test
    void getBaseClass() {
        assertEquals(String.class, ArrayUtils.getBaseClass(ARRAY.getClass()));
    }

    @Test
    void length() {
        assertEquals(ARRAY.length + ARRAY_WITH_NULL.length, ArrayUtils.length(ARRAY, ARRAY_WITH_NULL));
    }

}
