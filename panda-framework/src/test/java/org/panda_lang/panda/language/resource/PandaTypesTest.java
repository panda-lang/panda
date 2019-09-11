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

package org.panda_lang.panda.language.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PandaTypesTest {

    @Test
    void testConstants() {
        Assertions.assertEquals("void", PandaTypes.VOID.getName());
        Assertions.assertEquals("Boolean", PandaTypes.BOOLEAN.getName());
        Assertions.assertEquals("Char", PandaTypes.CHAR.getName());
        Assertions.assertEquals("Byte", PandaTypes.BYTE.getName());
        Assertions.assertEquals("Short", PandaTypes.SHORT.getName());
        Assertions.assertEquals("Int", PandaTypes.INT.getName());
        Assertions.assertEquals("Long", PandaTypes.LONG.getName());
        Assertions.assertEquals("Float", PandaTypes.FLOAT.getName());
        Assertions.assertEquals("Double", PandaTypes.DOUBLE.getName());

        Assertions.assertEquals("Object", PandaTypes.OBJECT.getName());
        Assertions.assertEquals("Array", PandaTypes.ARRAY.getName());

        Assertions.assertEquals("String", PandaTypes.STRING.getName());
        Assertions.assertEquals("Number", PandaTypes.NUMBER.getName());
        Assertions.assertEquals("Iterable", PandaTypes.ITERABLE.getName());
        Assertions.assertEquals("List", PandaTypes.LIST.getName());
    }

}