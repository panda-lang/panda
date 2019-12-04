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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayPrototype;
import org.panda_lang.framework.language.resource.internal.InternalModuleInfo;
import org.panda_lang.framework.language.resource.internal.PandaResourcesUtils;
import org.panda_lang.utilities.commons.ClassUtils;

public final class JavaModule implements InternalModuleInfo {

    @Override
    public void initialize(Module module) {
        PandaResourcesUtils.of(module, void.class, "void");
        PandaResourcesUtils.generate(module, Object.class);

        generate(module, boolean.class, "Bool");
        generate(module, char.class, "Char");
        generate(module, byte.class, "Byte");
        generate(module, short.class, "Short");
        generate(module, int.class, "Int");
        generate(module, long.class, "Long");
        generate(module, float.class, "Float");
        generate(module, double.class, "Double");
    }

    private void generate(Module module, Class<?> primitiveClass, String name) {
        Prototype primitiveType = PandaResourcesUtils.generate(module, primitiveClass, "Primitive" + name);
        Prototype type = PandaResourcesUtils.generate(module, ClassUtils.PRIMITIVE_EQUIVALENT.get(primitiveClass), name);

        type.addBase(primitiveType);
    }

    @Override
    public String[] getNames() {
        return new String[] {
                "String",
                "Number",
                "Iterable"
        };
    }

    @Override
    public String getPackageName() {
        return "java.lang";
    }

    @Override
    public String getModule() {
        return "java";
    }

}
