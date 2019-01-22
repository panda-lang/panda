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

class ObjectUtilsTest {

    private static final Object OBJECT = new Object();

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testIsNotNull() {
        Assertions.assertTrue(ObjectUtils.isNotNull(OBJECT));
        Assertions.assertFalse(ObjectUtils.isNotNull(null));
    }

    @Test
    public void testAreNull() {
        Assertions.assertFalse(ObjectUtils.areNull());
        Assertions.assertFalse(ObjectUtils.areNull(OBJECT, OBJECT, OBJECT));
        Assertions.assertFalse(ObjectUtils.areNull(OBJECT, null, OBJECT));

        Assertions.assertTrue(ObjectUtils.areNull(null, null, null));
    }

    @Test
    public void testEqualsOneOf() {
        Assertions.assertTrue(ObjectUtils.equalsOneOf("value", OBJECT, "value", OBJECT));
        Assertions.assertFalse(ObjectUtils.equalsOneOf("value", OBJECT, OBJECT, OBJECT));
    }

}
