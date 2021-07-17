/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package panda.utilities

import groovy.transform.CompileStatic
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target
import java.lang.reflect.Method

import static org.junit.jupiter.api.Assertions.assertAll

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class ReflectionUtilsTest {

    @Test
    void getMethod() {
        assertTrue(ReflectionUtils.getMethod(ReflectionUtilsTest.class, "getMethod").isPresent())
        assertFalse(ReflectionUtils.getMethod(ReflectionUtilsTest.class, "getMethodFake").isPresent())
    }

    @Test
    void getMethodsAnnotatedWith() {
        Set<Method> methods = ReflectionUtils.getMethodsAnnotatedWith(Bar.class, AnnotationTest.class)

        Assertions.assertEquals(2, methods.size())
        methods.forEach(method -> assertEquals("test", method.getName()))
    }

    @Test
    void getMethods() {
        List<Method> methods = ReflectionUtils.getMethods(Bar.class, "test")

        assertAll(
                () -> assertEquals(1, methods.size()),
                () -> assertEquals(Bar.class.getDeclaredMethod("test"), methods.get(0))
        )
    }

    @Test
    void getStaticFieldValues() {
        Collection<String> values = ReflectionUtils.getStaticFieldValues(Foo.class, String.class)

        assertEquals(1, values.size())
        assertEquals("static-value", values.iterator().next())
    }

    @Test
    void getFieldValues() {
        Collection<String> values = ReflectionUtils.getFieldValues(Bar.class, String.class, new Bar())

        assertEquals(1, values.size())
        assertEquals("value", values.iterator().next())
    }


    static class Foo {

        @SuppressWarnings("unused")
        private static final String STATIC_FIELD = "static-value";

        @AnnotationTest
        private void test() { assert false; }

    }

    static final class Bar extends Foo {

        @SuppressWarnings("unused")
        private final String FIELD = "value";

        @AnnotationTest
        private void test() { assert false; }

    }

}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface AnnotationTest {}
