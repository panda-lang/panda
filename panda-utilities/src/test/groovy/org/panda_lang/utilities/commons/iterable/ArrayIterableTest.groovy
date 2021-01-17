package org.panda_lang.utilities.commons.iterable

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class ArrayIterableTest {

    @Test
    void next_should_move_to_the_next_element_and_return_it() {
        ArrayIterable<String> arrayIterable = new ArrayIterable<>(new String[]{ "a", "b", "c" })
        Iterator<String> iterator = arrayIterable.iterator()

        assertTrue iterator.hasNext()
        assertEquals "a", iterator.next()
        assertEquals "b", iterator.next()
        assertEquals "c", iterator.next()
        assertFalse iterator.hasNext()
    }

}
