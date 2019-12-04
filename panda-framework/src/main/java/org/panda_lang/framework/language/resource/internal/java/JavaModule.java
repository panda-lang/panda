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

public final class JavaModule implements InternalModuleInfo {

    @Override
    public void initialize(Module module) {
        PandaResourcesUtils.of(module, void.class, "void");
        PandaResourcesUtils.generate(module, Object.class);
        PandaResourcesUtils.generate(module, Boolean.class, "Bool");

        Prototype intType = PandaResourcesUtils.generate(module, Integer.class, "Int");
        module.add(new ArrayPrototype(module, "PrimitiveInt", int.class, intType));

        Prototype charType = PandaResourcesUtils.generate(module, Character.class, "Char");
        module.add(new ArrayPrototype(module, "PrimitiveChar", char.class, charType));
    }

    @Override
    public String[] getNames() {
        return new String[] {
                "Byte",
                "Short",
                "Long",
                "Float",
                "Double",
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
