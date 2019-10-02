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

package org.panda_lang.framework.language.resource.internal.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JavaModuleTest {

    @Test
    void testConstants() {
        Assertions.assertEquals("void", JavaModule.VOID.getName());
        Assertions.assertEquals("Boolean", JavaModule.BOOLEAN.getName());
        Assertions.assertEquals("Char", JavaModule.CHAR.getName());
        Assertions.assertEquals("Byte", JavaModule.BYTE.getName());
        Assertions.assertEquals("Short", JavaModule.SHORT.getName());
        Assertions.assertEquals("Int", JavaModule.INT.getName());
        Assertions.assertEquals("Long", JavaModule.LONG.getName());
        Assertions.assertEquals("Float", JavaModule.FLOAT.getName());
        Assertions.assertEquals("Double", JavaModule.DOUBLE.getName());
        Assertions.assertEquals("Object", JavaModule.OBJECT.getName());
        Assertions.assertEquals("String", JavaModule.STRING.getName());
        Assertions.assertEquals("Number", JavaModule.NUMBER.getName());
    }

}