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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class PandaModuleLoader implements ModuleLoader {

    private final Collection<Module> importedModules;

    public PandaModuleLoader() {
        this.importedModules = new ArrayList<>();
    }

    public void include(Module module) {
        this.importedModules.add(module);
    }

    public @Nullable ClassPrototype forClass(String className) {
        if (StringUtils.isEmpty(className)) {
            return null;
        }

        if (className.contains(":")) {
            throw new PandaRuntimeException("Not implemented");
        }

        for (Module module : importedModules) {
            ClassPrototype prototype = module.get(className);

            if (prototype != null) {
                return prototype;
            }
        }

        return null;
    }

}
