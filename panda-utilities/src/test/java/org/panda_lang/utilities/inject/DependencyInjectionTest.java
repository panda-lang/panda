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

package org.panda_lang.utilities.inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.utilities.commons.ReflectionUtils;
import org.panda_lang.utilities.inject.annotations.Inject;
import org.panda_lang.utilities.inject.annotations.Injectable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("OptionalGetWithoutIsPresent")
final class DependencyInjectionTest {

    private static final String HELLO = "Hello";
    private static final String HELLO_AUTOWIRED = HELLO + " Autowired";
    private static final String INJECTOR_ARG = "Injector Arg";
    public static final int DYNAMIC = 7;

    private static final Map<String, String> MAP = new HashMap<String, String>() {{
        put("hello-autowired", HELLO_AUTOWIRED);
    }};

    @Test
    void testInjector() {
        AtomicBoolean limiterCalled = new AtomicBoolean(false);

        Injector injector = DependencyInjection.createInjector(resources -> {
            resources.on(String.class).assignInstance(HELLO);

            resources.annotatedWith(CustomAnnotation.class).assignHandler((expected, annotation, injectorArgs) -> {
                return MAP.get(annotation.value());
            });

            resources.processAnnotatedType(CustomAnnotation.class, String.class, (customAnnotation, parameter, value, injectorArgs) -> {
                Assertions.assertArrayEquals(new Object[] { INJECTOR_ARG }, injectorArgs);

                if (value.length() > (HELLO_AUTOWIRED.length() - 2)) {
                    limiterCalled.set(true);
                }

                return value;
            });
        });

        Assertions.assertDoesNotThrow(() -> {
            TestClass instance = injector.newInstance(TestClass.class);

            Method testTypeInvoke = ReflectionUtils.getMethod(TestClass.class, "testTypeInvoke", String.class).get();
            Assertions.assertEquals(HELLO, injector.invokeMethod(testTypeInvoke, instance));

            Method testAnnotationInvoke = ReflectionUtils.getMethod(TestClass.class, "testAnnotationInvoke", String.class).get();
            Assertions.assertEquals(HELLO_AUTOWIRED, injector.invokeMethod(testAnnotationInvoke, instance, INJECTOR_ARG));

            Method testForkedInjector = ReflectionUtils.getMethod(TestClass.class, "testForkedInjector", String.class, int.class).get();
            Assertions.assertEquals(DYNAMIC, (Integer) injector.fork(resources -> resources.on(int.class).assignInstance(DYNAMIC)).invokeMethod(testForkedInjector, instance));
        });

        Assertions.assertTrue(limiterCalled.get());
    }

    private static final class TestClass {

        @Inject
        TestClass(String value) {
            Assertions.assertEquals(HELLO, value);
        }

        @Inject
        public String testTypeInvoke(String value) {
            Assertions.assertEquals(HELLO, value);
            return value;
        }

        @Inject
        private String testAnnotationInvoke(@CustomAnnotation("hello-autowired") String value) {
            Assertions.assertEquals(HELLO_AUTOWIRED, value);
            return value;
        }

        @Inject
        int testForkedInjector(String value, int injectedOnInvoke) {
            Assertions.assertEquals(DYNAMIC, injectedOnInvoke);
            return injectedOnInvoke;
        }

    }

    @Injectable
    @Retention(RetentionPolicy.RUNTIME)
    private @interface CustomAnnotation {

        String value();

    }

}