package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

class ReflectionUtilsTest {

    @Test
    public void testGetMethodsAnnotatedWith() {
        List<Method> methods = ReflectionUtils.getMethodsAnnotatedWith(Bar.class, AnnotationTest.class);

        Assertions.assertEquals(2, methods.size());
        Assertions.assertEquals("test", methods.get(0).getName());
        Assertions.assertEquals("test", methods.get(1).getName());
    }

    @Test
    public void testGetMethods() {
        List<Method> methods = ReflectionUtils.getMethods(Bar.class, "test");

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, methods.size()),
                () -> Assertions.assertEquals(Bar.class.getDeclaredMethod("test"), methods.get(0))
        );
    }

}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnotationTest { }

class Foo {

    @AnnotationTest
    private void test() { }

}

class Bar extends Foo {

    @AnnotationTest
    private void test() { }

}
