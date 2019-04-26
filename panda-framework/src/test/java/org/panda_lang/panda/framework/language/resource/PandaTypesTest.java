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

package org.panda_lang.panda.framework.language.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PandaTypesTest {

    @Test
    void testConstants() {
        Assertions.assertEquals("void", PandaTypes.VOID.getClassName());
        Assertions.assertEquals("Boolean", PandaTypes.BOOLEAN.getClassName());
        Assertions.assertEquals("Char", PandaTypes.CHAR.getClassName());
        Assertions.assertEquals("Byte", PandaTypes.BYTE.getClassName());
        Assertions.assertEquals("Short", PandaTypes.SHORT.getClassName());
        Assertions.assertEquals("Int", PandaTypes.INT.getClassName());
        Assertions.assertEquals("Long", PandaTypes.LONG.getClassName());
        Assertions.assertEquals("Float", PandaTypes.FLOAT.getClassName());
        Assertions.assertEquals("Double", PandaTypes.DOUBLE.getClassName());

        Assertions.assertEquals("Object", PandaTypes.OBJECT.getClassName());
        Assertions.assertEquals("Array", PandaTypes.ARRAY.getClassName());

        Assertions.assertEquals("String", PandaTypes.STRING.getClassName());
        Assertions.assertEquals("Number", PandaTypes.NUMBER.getClassName());
        Assertions.assertEquals("Iterable", PandaTypes.ITERABLE.getClassName());
        Assertions.assertEquals("List", PandaTypes.LIST.getClassName());
    }

}