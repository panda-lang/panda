package org.panda_lang.utilities.commons.iterable

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class ReversedIterableTest {

    @Test
    void next_should_move_to_the_next_element_in_order_from_the_back_and_return_it() {
        ReversedIterable<String> reversedIterable = new ReversedIterable<>(Arrays.asList("a", "b", "c"))
        Iterator<String> iterator = reversedIterable.iterator()

        assertTrue iterator.hasNext()
        assertEquals "c", iterator.next()
        assertEquals "b", iterator.next()
        assertEquals "a", iterator.next()
        assertFalse iterator.hasNext()
    }

}
