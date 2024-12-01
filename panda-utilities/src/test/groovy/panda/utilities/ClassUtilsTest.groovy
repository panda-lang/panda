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

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertTrue

//@CompileStatic
class ClassUtilsTest {

    @Test
    void isAssignableFrom() {
        assertTrue(ClassUtils.isAssignableFrom(int.class, int.class))

        assertTrue(ClassUtils.isAssignableFrom(int.class, Integer.class))
        assertTrue(ClassUtils.isAssignableFrom(Integer.class, int.class))

        assertFalse(ClassUtils.isAssignableFrom(int.class, Number.class))
        assertTrue(ClassUtils.isAssignableFrom(Number.class, int.class))

        assertFalse(ClassUtils.isAssignableFrom(int.class, String.class))
    }

    @Test
    void exists() {
        assertTrue(ClassUtils.exists(String.class.getName()))
        assertFalse(ClassUtils.exists("xyz"))
    }

    @Test
    void forName() {
        assertTrue(ClassUtils.forName(String.class.getName()).isPresent())
        assertFalse(ClassUtils.forName("xyz").isPresent())
    }

    @Test
    void selectMostRelated() {
        Collection<Class<?>> classes = Arrays.asList(A.class, C.class)

        assertEquals(A.class, ClassUtils.selectMostRelated(classes, A.class).getOrNull())
        assertEquals(A.class, ClassUtils.selectMostRelated(classes, B.class).getOrNull())
        assertEquals(C.class, ClassUtils.selectMostRelated(classes, C.class).getOrNull())
    }

    static class A {}

    static class B extends A {}

    static final class C extends B {}

}
