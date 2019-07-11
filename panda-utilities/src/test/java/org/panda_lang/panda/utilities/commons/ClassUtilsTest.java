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

import java.util.Arrays;
import java.util.Collection;

class ClassUtilsTest {

    @Test
    void isAssignableFrom() {
        Assertions.assertTrue(ClassUtils.isAssignableFrom(int.class, int.class));

        Assertions.assertTrue(ClassUtils.isAssignableFrom(int.class, Integer.class));
        Assertions.assertTrue(ClassUtils.isAssignableFrom(Integer.class, int.class));

        Assertions.assertFalse(ClassUtils.isAssignableFrom(int.class, Number.class));
        Assertions.assertTrue(ClassUtils.isAssignableFrom(Number.class, int.class));

        Assertions.assertFalse(ClassUtils.isAssignableFrom(int.class, String.class));
    }

    @Test
    void exists() {
        Assertions.assertTrue(ClassUtils.exists(String.class.getName()));
        Assertions.assertFalse(ClassUtils.exists("xyz"));
    }

    @Test
    void forName() {
        Assertions.assertTrue(ClassUtils.forName(String.class.getName()).isPresent());
        Assertions.assertFalse(ClassUtils.forName("xyz").isPresent());
    }

    @Test
    void selectMostRelated() {
        Collection<Class<?>> classes = Arrays.asList(A.class, C.class);

        Assertions.assertEquals(A.class, ClassUtils.selectMostRelated(classes, A.class).orElse(null));
        Assertions.assertEquals(A.class, ClassUtils.selectMostRelated(classes, B.class).orElse(null));
        Assertions.assertEquals(C.class, ClassUtils.selectMostRelated(classes, C.class).orElse(null));
    }

    class A { }

    class B extends A { }

    class C extends B { }

}
