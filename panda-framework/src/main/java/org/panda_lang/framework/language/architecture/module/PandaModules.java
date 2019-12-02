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

package org.panda_lang.framework.language.architecture.module;

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.module.Modules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

abstract class PandaModules implements Modules {

    protected final Map<String, Module> modules = new HashMap<>();

    @Override
    public void include(Module module) {
        modules.put(module.getName(), module);
    }

    @Override
    public Optional<Module> get(String moduleQualifier, ModuleLoader loader) {
        if (!moduleQualifier.contains(":")) {
            return Optional.ofNullable(modules.get(moduleQualifier));
        }

        return PandaModulesUtils.fetch(loader, this, moduleQualifier, false);
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
