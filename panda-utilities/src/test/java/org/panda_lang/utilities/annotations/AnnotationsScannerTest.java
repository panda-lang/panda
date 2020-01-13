/*
 * Copyright (c) 2015-2020 Dzikoysk
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

package org.panda_lang.utilities.annotations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.panda_lang.utilities.annotations.monads.filters.JavaFilter;
import org.panda_lang.utilities.annotations.monads.filters.MavenFilter;
import org.panda_lang.utilities.annotations.monads.filters.PublicClassFileFilter;

import java.lang.reflect.Method;
import java.util.Collection;

final class AnnotationsScannerTest {

    private static AnnotationsScannerProcess process;

    @BeforeAll
    static void prepare() {
        AnnotationsScanner scanner = AnnotationsScanner.configuration()
                .includeClasses(AnnotationsScannerTest.class)
                .build();

        process = scanner.createProcess()
                .addDefaultProjectFilters("org.panda_lang")
                .addURLFilters(new JavaFilter())
                .addURLFilters(new MavenFilter())
                .addClassFileFilters(new PublicClassFileFilter())
                .fetch();
    }

    @Test
    void selectTypesAnnotatedWith() {
        Collection<Class<?>> classes = process.createSelector()
                .selectTypesAnnotatedWith(AnnotationTest.class);

        Assertions.assertEquals(1, classes.size());
        Assertions.assertEquals(Implementation.class, classes.iterator().next());
    }

    @Test
    void selectSubtypesOf() {
        Collection<Class<? extends TestType>> testTypeClasses = process.createSelector()
                .selectSubtypesOf(TestType.class);

        Assertions.assertEquals(3, testTypeClasses.size());
        Assertions.assertTrue(testTypeClasses.contains(WrappedTestType.class));
        Assertions.assertTrue(testTypeClasses.contains(Implementation.class));
        Assertions.assertTrue(testTypeClasses.contains(AnotherImplementation.class));

        Collection<Class<? extends WrappedTestType>> wrappedTestTypeClasses = process.createSelector()
                .selectSubtypesOf(WrappedTestType.class);

        Assertions.assertEquals(1, wrappedTestTypeClasses.size());
        Assertions.assertTrue(wrappedTestTypeClasses.contains(Implementation.class));
    }

    @Test
    void selectMethodsAnnotatedWith() {
        Collection<Method> annotatedMethods = process.createSelector()
                .selectMethodsAnnotatedWith(AnotherAnnotationTest.class);

        Assertions.assertEquals(1, annotatedMethods.size());
        Assertions.assertEquals("methodName", annotatedMethods.iterator().next().getName());
    }

    public @interface AnnotationTest {}

    public @interface AnotherAnnotationTest {}

    @SuppressWarnings("unused")
    interface NotVisibleType extends WrappedTestType {}

    public interface WrappedTestType extends TestType {}

    public interface TestType {}

    @AnnotationTest
    public static final class Implementation implements WrappedTestType {}

    public static final class AnotherImplementation implements TestType {

        @AnotherAnnotationTest
        public void methodName() { assert false; }

    }

}

