package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

    @Test
    public void testContains() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(ArrayUtils.contains(new String[] { "a", "b", "c" }, "b")),
                () -> Assertions.assertFalse(ArrayUtils.contains(new String[] { "a", "b", "c" }, "d"))
        );
    }

}
