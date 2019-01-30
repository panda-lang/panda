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

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.language.architecture.module.PandaModule;
import org.panda_lang.panda.framework.language.architecture.prototype.array.PandaArray;
import org.panda_lang.panda.framework.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.generator.ClassPrototypeGeneratorManager;

import java.util.ArrayList;
import java.util.List;

public class PandaTypes {

    private static final Module MODULE = new PandaModule(null);
    private static final ClassPrototypeGeneratorManager MAPPER = new ClassPrototypeGeneratorManager();

    public static final ClassPrototype VOID = PandaClassPrototype.of(MODULE, void.class, "void").fetch();
    public static final ClassPrototype BOOLEAN = PandaClassPrototype.of(MODULE, boolean.class, "Boolean").fetch();
    public static final ClassPrototype CHAR = PandaClassPrototype.of(MODULE, char.class, "Char").fetch();
    public static final ClassPrototype BYTE = PandaClassPrototype.of(MODULE, byte.class, "Byte").fetch();
    public static final ClassPrototype SHORT = PandaClassPrototype.of(MODULE, short.class, "Short").fetch();
    public static final ClassPrototype INT = PandaClassPrototype.of(MODULE, int.class, "Int").fetch();
    public static final ClassPrototype LONG = PandaClassPrototype.of(MODULE, int.class, "Long").fetch();
    public static final ClassPrototype FLOAT = PandaClassPrototype.of(MODULE, float.class, "Float").fetch();
    public static final ClassPrototype DOUBLE = PandaClassPrototype.of(MODULE, double.class, "Double").fetch();

    public static final ClassPrototype OBJECT = PandaClassPrototype.of(MODULE, Object.class, "Object").fetch();
    public static final ClassPrototype ARRAY = PandaClassPrototype.of(MODULE, PandaArray.class, "Array").fetch();

    public static final ClassPrototype STRING = MAPPER.generate(MODULE, String.class).fetch();
    public static final ClassPrototype NUMBER = MAPPER.generate(MODULE, Number.class).fetch();
    public static final ClassPrototype ITERABLE = MAPPER.generate(MODULE, Iterable.class).fetch();

    public void fill(ModulePath modulePath) {
        modulePath.addModule(MODULE);

        MODULE.add(MAPPER.generate(MODULE, List.class));
        MODULE.add(MAPPER.generate(MODULE, ArrayList.class));
        MODULE.add(MAPPER.generate(MODULE, StringBuilder.class));
    }

}
