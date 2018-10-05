package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

public class ReflectionUtilsTest {

    @Test
    public void testGetMethods() {
        List<Method> methods = ReflectionUtils.getMethods(ReflectionUtilsTest.class, "testMethod");

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, methods.size()),
                () -> Assertions.assertEquals(ReflectionUtilsTest.class.getDeclaredMethod("testMethod"), methods.get(0))
        );
    }

    private void testMethod() { }

}
