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

import java.lang.reflect.InvocationTargetException;

class DependencyInjectionTest {

    private static final String HELLO = "Hello Injector";

    @Test
    void testInjector() throws InvocationTargetException, IllegalAccessException {
        Injector injector = DependencyInjection.createInjector(new InjectorController() {
            @Override
            public void initialize(InjectorResources resources) {
                resources.on(String.class).assignInstance("Hello Injector");

                resources.annotatedWith(CustomAnnotation.class).assignHandler((expected, annotation) -> {
                    System.out.println(annotation.value());
                    // np. ret map.get(value)
                    return null;
                });
            }
        });

        String result = injector.invokeMethod(ReflectionUtils.getMethods(TestClass.class, "testTypeInvoke").get(0), new TestClass());
        Assertions.assertEquals(HELLO, result);
    }

    private class TestClass {

        public String testTypeInvoke(String hello) {
            Assertions.assertEquals(HELLO, hello);
            return hello;
        }

        public String testAnnotationInvoke(@CustomAnnotation("hello") String hello) {
            Assertions.assertEquals(HELLO, hello);
            return hello;
        }

    }

    private @interface CustomAnnotation {

        String value();

    }

}