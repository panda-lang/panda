package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ValidationUtilsTest {
    @Test
    void notNull__null_argument_throws_exception() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> ValidationUtils.notNull(null)
        );
    }

    @Test
    void notNull__non_null_argument_returns_argument() {
        final String value = "testValue";
        String actual = ValidationUtils.notNull(value);

        Assertions.assertEquals(value, actual);
    }
}