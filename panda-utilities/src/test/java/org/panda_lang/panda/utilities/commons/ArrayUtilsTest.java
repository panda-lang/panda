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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

    private static final String[] EMPTY_ARRAY = new String[] { };

    private static final String[] ARRAY = new String[] { "a", "b", "c" };

    private static final String[] ARRAY_WITH_NULL = new String[] { "a", "b", "c", null };

    @Test
    void containsNull() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(ArrayUtils.containsNull(ARRAY_WITH_NULL)),

                () -> Assertions.assertFalse(ArrayUtils.containsNull(ARRAY)),
                () -> Assertions.assertFalse(ArrayUtils.containsNull(EMPTY_ARRAY))
        );
    }

    @Test
    void contains() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(ArrayUtils.contains(ARRAY, "b")),

                () -> Assertions.assertFalse(ArrayUtils.contains(EMPTY_ARRAY, "d")),
                () -> Assertions.assertFalse(ArrayUtils.contains(ARRAY_WITH_NULL, "d"))
        );
    }

    @Test
    void mergeArrays() {
        Assertions.assertArrayEquals(new String[] {
                "a", "b", "c",
                "a", "b", "c", null
        }, ArrayUtils.mergeArrays(ARRAY, ARRAY_WITH_NULL));
    }

    @Test
    void asList() {
        Assertions.assertEquals(1, ArrayUtils.asList("a", null).size());
        Assertions.assertEquals(1, ArrayUtils.asList(ArrayUtils.of(null, "a")).size());
        Assertions.assertEquals(ARRAY_WITH_NULL.length - 1, ArrayUtils.asList(ARRAY_WITH_NULL).size());
    }

    @Test
    void isEmpty() {
        Assertions.assertTrue(ArrayUtils.isEmpty(EMPTY_ARRAY));
    }

    @Test
    void getArrayClass() {
        Assertions.assertEquals(String[].class, ArrayUtils.getArrayClass(String.class));
    }

    @Test
    void getDimensionalArrayType() {
        Assertions.assertEquals(String[].class, ArrayUtils.getDimensionalArrayType(String.class, 2));
    }

    @Test
    void of() {
        Assertions.assertArrayEquals(new String[] { "a", "b" }, ArrayUtils.of("a", "b"));
    }

    @Test
    void get() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(ArrayUtils.get(ARRAY, 0)),
                () -> Assertions.assertNotNull(ArrayUtils.get(ARRAY, 2)),

                () -> Assertions.assertNull(ArrayUtils.get(ARRAY, -1)),
                () -> Assertions.assertNull(ArrayUtils.get(ARRAY, 3))
        );
    }

    @Test
    void dimensionalArrayClass() {
        Assertions.assertAll(
                () -> Assertions.assertEquals(Object[][].class, ArrayUtils.getDimensionalArrayType(Object.class, 3)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> ArrayUtils.getDimensionalArrayType(Object.class, 0))
        );
    }

}
