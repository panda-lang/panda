package org.panda_lang.panda.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
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

    @Test
    public void testStaticFieldValues() {
        Collection<String> values = ReflectionUtils.getStaticFieldValues(Foo.class, String.class);

        Assertions.assertEquals(1, values.size());
        Assertions.assertEquals("static-value", values.iterator().next());
    }

    @Test
    public void testFieldValues() {
        Collection<String> values = ReflectionUtils.getFieldValues(Bar.class, String.class, new Bar());

        Assertions.assertEquals(1, values.size());
        Assertions.assertEquals("value", values.iterator().next());
    }

}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnotationTest { }

class Foo {

    private static final String STATIC_FIELD = "static-value";

    @AnnotationTest
    private void test() { }

}

class Bar extends Foo {

    private final String FIELD = "value";

    @AnnotationTest
    private void test() { }

}
