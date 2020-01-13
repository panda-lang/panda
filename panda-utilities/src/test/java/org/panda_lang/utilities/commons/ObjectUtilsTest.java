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

package org.panda_lang.utilities.commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ObjectUtilsTest {

    private static final Object OBJECT = new Object();

    @Test
    @SuppressWarnings("ConstantConditions")
    void isNotNull() {
        Assertions.assertTrue(ObjectUtils.isNotNull(OBJECT));
        Assertions.assertFalse(ObjectUtils.isNotNull(null));
    }

    @Test
    void areNull() {
        Assertions.assertFalse(ObjectUtils.areNull());
        Assertions.assertFalse(ObjectUtils.areNull(OBJECT, OBJECT, OBJECT));
        Assertions.assertFalse(ObjectUtils.areNull(OBJECT, null, OBJECT));

        Assertions.assertTrue(ObjectUtils.areNull(null, null, null));
    }

    @Test
    void equalsOneOf() {
        Assertions.assertTrue(ObjectUtils.equalsOneOf("value", OBJECT, "value", OBJECT));
        Assertions.assertFalse(ObjectUtils.equalsOneOf("value", OBJECT, OBJECT, OBJECT));
    }

    @Test
    void cast() {
        Assertions.assertEquals("text", ObjectUtils.cast("text"));
        Assertions.assertNull(ObjectUtils.cast(String.class, 5));

        Object unknown = "content";
        Assertions.assertEquals("content", ObjectUtils.cast(String.class, unknown));

        Assertions.assertDoesNotThrow(() -> {
            @SuppressWarnings("unused")
            String value = ObjectUtils.cast(null);
        });
    }

    @Test
    void testEquals() {
        Assertions.assertFalse(ObjectUtils.equals(5, "value", (a, b) -> true));
        Object aObject = new Object();
        Assertions.assertTrue(ObjectUtils.equals(aObject, aObject.toString(), aObject, Object::toString));
    }

}
