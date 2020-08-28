package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

class ReversedIterableTest {

    @Test
    void next_should_moves_to_the_next_element_in_order_from_the_back_and_return_it() {
        ReversedIterable<String> reversedIterable = new ReversedIterable<>(Arrays.asList("a", "b", "c"));

        Iterator<String> iterator = reversedIterable.iterator();

        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("c", iterator.next());
        Assertions.assertEquals("b", iterator.next());
        Assertions.assertEquals("a", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

}