package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class ListUtilsTest {

    private static final List<String> LIST = Arrays.asList("a", "b", "c");

    @Test
    public void testGet() {
        Assertions.assertAll(
                () -> Assertions.assertNotNull(ListUtils.get(LIST, 0)),
                () -> Assertions.assertNotNull(ListUtils.get(LIST, 2)),

                () -> Assertions.assertNull(ListUtils.get(LIST, -1)),
                () -> Assertions.assertNull(ListUtils.get(LIST, 3))
        );
    }

}
