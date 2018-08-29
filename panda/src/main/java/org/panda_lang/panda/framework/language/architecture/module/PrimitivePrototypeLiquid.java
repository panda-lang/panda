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

package org.panda_lang.panda.framework.language.architecture.module;

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.generator.ClassPrototypeGeneratorManager;

import java.util.ArrayList;
import java.util.List;

public class PrimitivePrototypeLiquid {

    public static final ClassPrototype VOID = PandaClassPrototype.of(void.class, "void");
    public static final ClassPrototype BOOLEAN = PandaClassPrototype.of(boolean.class, "Boolean");
    public static final ClassPrototype CHAR = PandaClassPrototype.of(char.class, "Char");
    public static final ClassPrototype BYTE = PandaClassPrototype.of(byte.class, "Byte");
    public static final ClassPrototype SHORT = PandaClassPrototype.of(short.class, "Short");
    public static final ClassPrototype INT = PandaClassPrototype.of(int.class, "Int");
    public static final ClassPrototype LONG = PandaClassPrototype.of(int.class, "Long");
    public static final ClassPrototype FLOAT = PandaClassPrototype.of(float.class, "Float");
    public static final ClassPrototype DOUBLE = PandaClassPrototype.of(double.class, "Double");
    public static final ClassPrototype OBJECT = PandaClassPrototype.of(Object.class);

    private static final ClassPrototypeGeneratorManager MAPPER = new ClassPrototypeGeneratorManager();

    public void fill(ModulePath modulePath) {
        Module defaultModule = modulePath.create((String) null);

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

        defaultModule.add(MAPPER.generate(modulePath, String.class));
        defaultModule.add(MAPPER.generate(modulePath, List.class));
        defaultModule.add(MAPPER.generate(modulePath, ArrayList.class));
        defaultModule.add(MAPPER.generate(modulePath, StringBuilder.class));
    }

}
