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

package org.panda_lang.framework.language.resource;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.framework.language.architecture.module.PandaModule;
import org.panda_lang.framework.language.architecture.prototype.array.PandaArray;
import org.panda_lang.framework.language.architecture.prototype.PandaPrototype;
import org.panda_lang.framework.language.architecture.prototype.generator.ClassPrototypeGeneratorManager;

import java.util.ArrayList;
import java.util.List;

public final class PandaTypes {

    private static final Module MODULE = new PandaModule(null);

    public static final Prototype VOID = PandaPrototype.of(MODULE, void.class, "void").fetch();
    public static final Prototype BOOLEAN = PandaPrototype.of(MODULE, Boolean.class, "Boolean").fetch();
    public static final Prototype CHAR = PandaPrototype.of(MODULE, Character.class, "Char").fetch();
    public static final Prototype BYTE = PandaPrototype.of(MODULE, Byte.class, "Byte").fetch();
    public static final Prototype SHORT = PandaPrototype.of(MODULE, Short.class, "Short").fetch();
    public static final Prototype INT = PandaPrototype.of(MODULE, Integer.class, "Int").fetch();
    public static final Prototype LONG = PandaPrototype.of(MODULE, Long.class, "Long").fetch();
    public static final Prototype FLOAT = PandaPrototype.of(MODULE, Float.class, "Float").fetch();
    public static final Prototype DOUBLE = PandaPrototype.of(MODULE, Double.class, "Double").fetch();

    public static final Prototype OBJECT = PandaPrototype.of(MODULE, Object.class, "Object").fetch();
    public static final Prototype ARRAY = PandaPrototype.of(MODULE, PandaArray.class, "Array").fetch();

    public static final Prototype STRING = of(String.class).fetch();
    public static final Prototype NUMBER = of(Number.class).fetch();
    public static final Prototype ITERABLE = of(Iterable.class).fetch();
    public static final Prototype LIST = of(List.class).fetch();

    public static final Prototype THROWABLE = of(Throwable.class).fetch();
    public static final Prototype EXCEPTION = of(Exception.class).fetch();
    public static final Prototype RUNTIME_EXCEPTION = of(RuntimeException.class).fetch();

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

    private static PrototypeReference of(Class<?> clazz) {
        return ClassPrototypeGeneratorManager.getInstance().generate(MODULE, clazz, clazz.getSimpleName());
    }

}
