/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.module;

import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract class PandaModuleContainer implements ModuleContainer {

    protected final Map<String, Module> modules = new HashMap<>();

    @Override
    public Module include(Module module) {
        modules.put(module.getSimpleName(), module);
        return module;
    }

    @Override
    public Option<Module> forModule(String moduleQualifier) {
        if (moduleQualifier.contains("::")) {
            throw new PandaFrameworkException("Cannot get module using qualifier with type operator");
        }

        if (!moduleQualifier.contains(":")) {
            return Option.of(modules.get(moduleQualifier));
        }

        return fetch(moduleQualifier, false);
    }

    @Override
    public Module acquire(String moduleQualifier) {
        return fetch(moduleQualifier, true).orThrow(() -> {
            throw new PandaFrameworkException("Cannot create module " + moduleQualifier);
        });
    }

    protected Option<Module> fetch(String moduleQualifier, boolean compute) {
        String[] names = moduleQualifier.split(":");
        ModuleContainer currentContainer = this;
        Module currentModule = null;

        for (String name : names) {
            if (StringUtils.isEmpty(name)) {
                throw new PandaFrameworkException("Illegal name " + moduleQualifier);
            }

            Module nextModule = currentContainer.forModule(name).getOrNull();

            if (nextModule == null && compute) {
                nextModule = new PandaModule(currentModule, name);
                currentContainer.include(nextModule);
            }

            if (nextModule == null) {
                return Option.none();
            }

            currentModule = nextModule;
            currentContainer = currentModule;
        }

        return Option.of(currentModule);
    }

    public static Option<Type> forName(Collection<? extends TypeResolver> resources, String name) {
        return PandaStream.of(resources)
                .flatMap(parent -> parent.forType(name))
                .head();
    }

    @Override
    public Collection<? extends String> getNames() {
        return modules.keySet();
    }

    @Override
    public Collection<? extends Module> getModules() {
        return modules.values();
    }

}
