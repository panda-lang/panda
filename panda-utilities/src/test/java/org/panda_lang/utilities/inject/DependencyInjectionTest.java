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
import org.panda_lang.utilities.inject.annotations.Wired;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("OptionalGetWithoutIsPresent")
final class DependencyInjectionTest {

    private static final String HELLO = "Hello";
    private static final String HELLO_WIRED = HELLO + " Wired";
    private static final String HELLO_AUTOWIRED = HELLO + " Autowired";

    private static final Map<String, String> MAP = new HashMap<String, String>() {{
        put("hello-autowired", HELLO_AUTOWIRED);
        put("hello-wired", HELLO_WIRED);
    }};

    @Test
    void testInjector() {
        Injector injector = DependencyInjection.createInjector(resources -> {
            resources.on(String.class).assignInstance(HELLO);

            resources.annotatedWith(CustomAnnotation.class).assignHandler((expected, annotation) -> {
                return MAP.get(annotation.value());
            });

            resources.annotatedWithMetadata(CustomAnnotation.class).assignHandler((expected, annotation) -> {
                return MAP.get(annotation.getMetadata().getValue());
            });
        });

        Assertions.assertDoesNotThrow(() -> {
            TestClass instance = injector.newInstance(TestClass.class);
            Assertions.assertEquals(HELLO, injector.invokeMethod(ReflectionUtils.getMethod(TestClass.class, "testTypeInvoke", String.class).get(), instance));
            Assertions.assertEquals(HELLO_AUTOWIRED, injector.invokeMethod(ReflectionUtils.getMethod(TestClass.class, "testAnnotationInvoke", String.class).get(), instance));
            Assertions.assertEquals(HELLO_WIRED, injector.invokeMethod(ReflectionUtils.getMethod(TestClass.class, "testWiredInvoke", String.class).get(), instance));
        });
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

        @Wired({
                @Wired.Link(parameter = "value", with = CustomAnnotation.class, value = "hello-wired")
        })
        protected String testWiredInvoke(String value) {
            Assertions.assertEquals(HELLO_WIRED, value);
            return value;
        }

    }

    @Injectable
    @Retention(RetentionPolicy.RUNTIME)
    private @interface CustomAnnotation {

        String value();

    }

}