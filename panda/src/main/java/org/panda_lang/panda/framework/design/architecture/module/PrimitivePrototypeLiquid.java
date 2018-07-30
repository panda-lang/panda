/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.design.architecture.module;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.generator.ClassPrototypeGeneratorManager;

public class PrimitivePrototypeLiquid {

    public static final ClassPrototype VOID = new PandaClassPrototype(void.class, "void");
    public static final ClassPrototype BOOLEAN = new PandaClassPrototype(boolean.class, "Boolean");
    public static final ClassPrototype CHAR = new PandaClassPrototype(char.class, "Char");
    public static final ClassPrototype BYTE = new PandaClassPrototype(byte.class, "Byte");
    public static final ClassPrototype SHORT = new PandaClassPrototype(short.class, "Short");
    public static final ClassPrototype INT = new PandaClassPrototype(int.class, "Int");
    public static final ClassPrototype LONG = new PandaClassPrototype(int.class, "Long");
    public static final ClassPrototype FLOAT = new PandaClassPrototype(float.class, "Float");
    public static final ClassPrototype DOUBLE = new PandaClassPrototype(double.class, "Double");

    private static final ClassPrototypeGeneratorManager MAPPER = new ClassPrototypeGeneratorManager();

    public static final ClassPrototype OBJECT = new PandaClassPrototype(Object.class);
    public static final ClassPrototype STRING = new PandaClassPrototype(String.class);

    public void fill(ModulePath registry) {
        Module defaultModule = registry.create((String) null);

        defaultModule.add(VOID);
        defaultModule.add(BOOLEAN);
        defaultModule.add(CHAR);
        defaultModule.add(BYTE);
        defaultModule.add(SHORT);
        defaultModule.add(INT);
        defaultModule.add(LONG);
        defaultModule.add(FLOAT);
        defaultModule.add(DOUBLE);
        defaultModule.add(OBJECT);
        defaultModule.add(STRING);
    }

}
