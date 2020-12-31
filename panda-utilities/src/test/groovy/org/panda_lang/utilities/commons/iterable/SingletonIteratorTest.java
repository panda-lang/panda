package org.panda_lang.utilities.commons.iterable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SingletonIteratorTest {

    @Test
    void next_should_return_singleton_element() {
        SingletonIterator<String> reversedIterable = new SingletonIterator<>("a");

        Assertions.assertTrue(reversedIterable.hasNext());
        Assertions.assertEquals("a", reversedIterable.next());
        Assertions.assertFalse(reversedIterable.hasNext());
    }

}