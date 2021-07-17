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

import groovy.transform.CompileStatic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow
import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertFalse
import static org.junit.jupiter.api.Assertions.assertNull
import static org.junit.jupiter.api.Assertions.assertTrue

@CompileStatic
final class ObjectUtilsTest {

    private static final Object OBJECT = new Object()

    @Test
    @SuppressWarnings("ConstantConditions")
    void isNotNull() {
        assertTrue(ObjectUtils.isNotNull(OBJECT))
        assertFalse(ObjectUtils.isNotNull(null))
    }

    @Test
    void areNull() {
        assertFalse(ObjectUtils.areNull())
        assertFalse(ObjectUtils.areNull(OBJECT, OBJECT, OBJECT))
        assertFalse(ObjectUtils.areNull(OBJECT, null, OBJECT))

        assertTrue(ObjectUtils.areNull(null, null, null))
    }

    @Test
    void equalsOneOf() {
        assertTrue(ObjectUtils.equalsOneOf("value", OBJECT, "value", OBJECT))
        assertFalse(ObjectUtils.equalsOneOf("value", OBJECT, OBJECT, OBJECT))
    }

    @Test
    void cast() {
        assertEquals("text", ObjectUtils.cast("text"))
        assertNull(ObjectUtils.cast(String.class, 5))

        Object unknown = "content";
        assertEquals("content", ObjectUtils.cast(String.class, unknown))

        assertDoesNotThrow({
            @SuppressWarnings("unused")
            String value = ObjectUtils.cast(null)
        } as Executable)
    }

    @Test
    void testEquals() {
        assertFalse(ObjectUtils.equals(5, "value", (a, b) -> true))
        Object aObject = new Object()
        assertTrue(ObjectUtils.equals(aObject, aObject.toString(), aObject, value -> value.toString()))
    }

}
