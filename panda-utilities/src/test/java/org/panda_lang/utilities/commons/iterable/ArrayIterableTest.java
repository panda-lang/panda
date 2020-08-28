package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

class ArrayIterableTest {

    @Test
    void next_should_moves_to_the_next_element_and_return_it() {
        ArrayIterable<String> arrayIterable = new ArrayIterable<>(new String[]{"a", "b", "c"});

        Iterator<String> iterator = arrayIterable.iterator();

        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals("a", iterator.next());
        Assertions.assertEquals("b", iterator.next());
        Assertions.assertEquals("c", iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

}