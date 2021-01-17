package org.panda_lang.utilities.commons

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertThrows

@CompileStatic
class ValidationUtilsTest {

    @Test
    void notNull__null_argument_throws_exception() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ValidationUtils.notNull(null)
        )
    }

    @Test
    void notNull__non_null_argument_returns_argument() {
        assertEquals "testValue", ValidationUtils.notNull("testValue")
    }

}