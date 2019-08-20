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
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.language.architecture.module.PandaModule;
import org.panda_lang.panda.framework.language.architecture.prototype.array.PandaArray;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.PandaClassPrototype;
import org.panda_lang.panda.framework.language.architecture.prototype.standard.generator.ClassPrototypeGeneratorManager;

import java.util.ArrayList;
import java.util.List;

public class PandaTypes {

    private static final Module MODULE = new PandaModule(null);

    public static final ClassPrototype VOID = PandaClassPrototype.of(MODULE, void.class, "void").fetch();
    public static final ClassPrototype BOOLEAN = PandaClassPrototype.of(MODULE, Boolean.class, "Boolean").fetch();
    public static final ClassPrototype CHAR = PandaClassPrototype.of(MODULE, Character.class, "Char").fetch();
    public static final ClassPrototype BYTE = PandaClassPrototype.of(MODULE, Byte.class, "Byte").fetch();
    public static final ClassPrototype SHORT = PandaClassPrototype.of(MODULE, Short.class, "Short").fetch();
    public static final ClassPrototype INT = PandaClassPrototype.of(MODULE, Integer.class, "Int").fetch();
    public static final ClassPrototype LONG = PandaClassPrototype.of(MODULE, Long.class, "Long").fetch();
    public static final ClassPrototype FLOAT = PandaClassPrototype.of(MODULE, Float.class, "Float").fetch();
    public static final ClassPrototype DOUBLE = PandaClassPrototype.of(MODULE, Double.class, "Double").fetch();

    public static final ClassPrototype OBJECT = PandaClassPrototype.of(MODULE, Object.class, "Object").fetch();
    public static final ClassPrototype ARRAY = PandaClassPrototype.of(MODULE, PandaArray.class, "Array").fetch();

    public static final ClassPrototype STRING = of(String.class).fetch();
    public static final ClassPrototype NUMBER = of(Number.class).fetch();
    public static final ClassPrototype ITERABLE = of(Iterable.class).fetch();
    public static final ClassPrototype LIST = of(List.class).fetch();

    public static final ClassPrototype THROWABLE = of(Throwable.class).fetch();
    public static final ClassPrototype EXCEPTION = of(Exception.class).fetch();
    public static final ClassPrototype RUNTIME_EXCEPTION = of(RuntimeException.class).fetch();

    static {
        of(ArrayList.class);
        of(StringBuilder.class);
    }

    public ModulePath fill(ModulePath modulePath) {
        MODULE.getReferences().forEach(reference -> {
            modulePath.getDefaultModule().add(reference);
        });

        return modulePath;
    }

    private static ClassPrototypeReference of(Class<?> clazz) {
        return ClassPrototypeGeneratorManager.getInstance().generate(MODULE, clazz, clazz.getSimpleName());
    }

}
