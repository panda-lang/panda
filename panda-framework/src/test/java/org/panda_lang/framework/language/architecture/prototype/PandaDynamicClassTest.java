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

package org.panda_lang.framework.language.architecture.prototype;

import org.junit.jupiter.api.Test;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.language.architecture.module.PandaModule;

import static org.junit.jupiter.api.Assertions.*;

class PandaDynamicClassTest {

    private static class CustomObject { }

    private static final Module MODULE = new PandaModule("MODULE", null);

    private static final DynamicClass DYNAMIC_CLASS = new PandaDynamicClass(MODULE, "ExampleClass", CustomObject.class);

    @Test
    void reimplement() {
        DynamicClass dynamicClass = new PandaDynamicClass(MODULE, "ExampleClass", Object.class);
        assertEquals(Object.class, dynamicClass.getImplementation());

        dynamicClass.reimplement(CustomObject.class);
        assertEquals(CustomObject.class, dynamicClass.getImplementation());
    }

    @Test
    void isAssignableFrom() {
        DynamicClass anotherClass = new PandaDynamicClass(MODULE, "AnotherClass", Object.class);
        assertFalse(DYNAMIC_CLASS.isAssignableFrom(anotherClass));

        anotherClass.reimplement(CustomObject.class);
        assertTrue(DYNAMIC_CLASS.isAssignableFrom(anotherClass));
    }

    @Test
    void getImplementation() {
        assertEquals(CustomObject.class, DYNAMIC_CLASS.getImplementation());
    }

    @Test
    void getModule() {
        assertEquals("MODULE", DYNAMIC_CLASS.getModule());
    }

    @Test
    void getSimpleName() {
        assertEquals("ExampleClass", DYNAMIC_CLASS.getSimpleName());
    }

    @Test
    void getName() {
        assertEquals("MODULE.ExampleClass", DYNAMIC_CLASS.getName());
    }

}