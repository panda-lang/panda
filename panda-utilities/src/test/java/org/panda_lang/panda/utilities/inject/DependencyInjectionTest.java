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

package org.panda_lang.panda.utilities.inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.panda_lang.panda.utilities.commons.ReflectionUtils;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.util.Map;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class DependencyInjectionTest {

    private static final String HELLO = "Hello Injector";
    private static final Map<String, String> MAP = Maps.of("hello", HELLO);

    @Test
    void testInjector() {
        Injector injector = DependencyInjection.createInjector(resources -> {
            resources.on(String.class).assignInstance(HELLO);

            resources.annotatedWith(CustomAnnotation.class).assignHandler((expected, annotation) -> {
                return MAP.get(annotation.value());
            });
        });

        TestClass instance = injector.newInstance(TestClass.class);
        Assertions.assertEquals(HELLO, injector.invokeMethod(ReflectionUtils.getMethod(TestClass.class, "testTypeInvoke", String.class).get(), instance));
        Assertions.assertEquals(HELLO, injector.invokeMethod(ReflectionUtils.getMethod(TestClass.class, "testAnnotationInvoke", String.class).get(), instance));
    }

    private static class TestClass {

        TestClass(String value) {
            Assertions.assertEquals(HELLO, value);
        }

        private String testTypeInvoke(String value) {
            Assertions.assertEquals(HELLO, value);
            return value;
        }

        protected String testAnnotationInvoke(@CustomAnnotation("hello") String value) {
            Assertions.assertEquals(HELLO, value);
            return value;
        }

    }

    private @interface CustomAnnotation {

        String value();

    }

}