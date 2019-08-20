/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import java.util.Set;

final class ReflectionUtilsTest {

    @Test
    void getMethod() {
        Assertions.assertTrue(ReflectionUtils.getMethod(ReflectionUtilsTest.class, "getMethod").isPresent());
        Assertions.assertFalse(ReflectionUtils.getMethod(ReflectionUtilsTest.class, "getMethodFake").isPresent());
    }

    @Test
    void getMethodsAnnotatedWith() {
        Set<Method> methods = ReflectionUtils.getMethodsAnnotatedWith(Bar.class, AnnotationTest.class);

        Assertions.assertEquals(2, methods.size());
        methods.forEach(method -> Assertions.assertEquals("test", method.getName()));
    }

    @Test
    void getMethods() {
        List<Method> methods = ReflectionUtils.getMethods(Bar.class, "test");

        Assertions.assertAll(
                () -> Assertions.assertEquals(1, methods.size()),
                () -> Assertions.assertEquals(Bar.class.getDeclaredMethod("test"), methods.get(0))
        );
    }

    @Test
    void getStaticFieldValues() {
        Collection<String> values = ReflectionUtils.getStaticFieldValues(Foo.class, String.class);

        Assertions.assertEquals(1, values.size());
        Assertions.assertEquals("static-value", values.iterator().next());
    }

    @Test
    void getFieldValues() {
        Collection<String> values = ReflectionUtils.getFieldValues(Bar.class, String.class, new Bar());

        Assertions.assertEquals(1, values.size());
        Assertions.assertEquals("value", values.iterator().next());
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface AnnotationTest {}

    static class Foo {

        private static final String STATIC_FIELD = "static-value";

        @AnnotationTest
        private void test() { }

    }

    static class Bar extends Foo {

        private final String FIELD = "value";

        @AnnotationTest
        private void test() { }

    }

}