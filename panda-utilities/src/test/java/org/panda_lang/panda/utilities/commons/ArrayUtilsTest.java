package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

    private static final String[] EMPTY_ARRAY = new String[] { };

    private static final String[] ARRAY = new String[] { "a", "b", "c" };

    private static final String[] ARRAY_WITH_NULL = new String[] { "a", "b", "c", null };

    @Test
    public void testContainsNull() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(ArrayUtils.containsNull(ARRAY_WITH_NULL)),

                () -> Assertions.assertFalse(ArrayUtils.containsNull(ARRAY)),
                () -> Assertions.assertFalse(ArrayUtils.containsNull(EMPTY_ARRAY))
        );
    }

    @Test
    public void testContains() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(ArrayUtils.contains(ARRAY, "b")),

                () -> Assertions.assertFalse(ArrayUtils.contains(EMPTY_ARRAY, "d")),
                () -> Assertions.assertFalse(ArrayUtils.contains(ARRAY_WITH_NULL, "d"))
        );
    }

    @Test
    public void testGet() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(ArrayUtils.get(ARRAY, 0)),
                () -> Assertions.assertNotNull(ArrayUtils.get(ARRAY, 2)),

                () -> Assertions.assertNull(ArrayUtils.get(ARRAY, -1)),
                () -> Assertions.assertNull(ArrayUtils.get(ARRAY, 3))
        );
    }

}
