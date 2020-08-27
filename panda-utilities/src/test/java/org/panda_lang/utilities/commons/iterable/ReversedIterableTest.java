package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

class ReversedIterableTest {

    @Test
    void testReservedIterable() {
        ReversedIterable<String> reversedIterable =
                new ReversedIterable<>(Arrays.asList("a", "b", "c"));


        Iterator<String> iterator = reversedIterable.iterator();


        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("c", iterator.next());
        Assertions.assertEquals("b", iterator.next());
        Assertions.assertEquals("a", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

}