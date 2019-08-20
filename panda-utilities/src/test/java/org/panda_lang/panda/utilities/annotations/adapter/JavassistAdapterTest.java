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

package org.panda_lang.panda.utilities.annotations.adapter;

import javassist.bytecode.ClassFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class JavassistAdapterTest {

    private static final JavassistAdapter ADAPTER = new JavassistAdapter();
    private static final ClassFile CLASS_FILE = new ClassFile(false, "Test", null);

    public static class Foo { }

    @Test
    void acceptsInput() {
        Assertions.assertTrue(ADAPTER.acceptsInput("Test.class"));
        Assertions.assertFalse(ADAPTER.acceptsInput("Test"));
    }

    @Test
    void getClassName() {
        Assertions.assertEquals("Test", ADAPTER.getClassName(CLASS_FILE));
    }

    @Test
    void isPublic() {
        Assertions.assertFalse(ADAPTER.isPublic(null));
        Assertions.assertFalse(ADAPTER.isPublic(CLASS_FILE));
        Assertions.assertTrue(ADAPTER.isPublic(Foo.class));
    }

    @Test
    void getFields() {
        Assertions.assertTrue(ADAPTER.getFields(CLASS_FILE).isEmpty());
    }

    @Test
    void getMethods() {
        Assertions.assertTrue(ADAPTER.getMethods(CLASS_FILE).isEmpty());
    }

}