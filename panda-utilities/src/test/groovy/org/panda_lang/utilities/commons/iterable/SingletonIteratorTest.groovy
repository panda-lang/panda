package org.panda_lang.utilities.commons.iterable

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*

@CompileStatic
final class SingletonIteratorTest {

    @Test
    void next_should_return_singleton_element() {
        SingletonIterator<String> reversedIterable = new SingletonIterator<>("a")

        assertTrue reversedIterable.hasNext()
        assertEquals "a", reversedIterable.next()
        assertFalse(reversedIterable.hasNext())
    }

}