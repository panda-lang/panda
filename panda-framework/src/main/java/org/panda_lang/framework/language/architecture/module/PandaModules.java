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

package org.panda_lang.framework.language.architecture.module;

import io.vavr.control.Option;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.Modules;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

abstract class PandaModules implements Modules {

    protected final Map<String, Module> modules = new HashMap<>();

    @Override
    public void include(Module module) {
        modules.put(module.getName(), module);
    }

    @Override
    public Option<Module> get(String moduleQualifier) {
        if (!moduleQualifier.contains(":")) {
            return Option.of(modules.get(moduleQualifier));
        }

        return fetch(moduleQualifier, false);
    }

    @Override
    public Module allocate(String moduleQualifier) {
        return fetch(moduleQualifier, true).getOrElseThrow(() -> {
            throw new PandaFrameworkException("Cannot create module " + moduleQualifier);
        });
    }

    protected Option<Module> fetch(String moduleQualifier, boolean compute) {
        String[] names = moduleQualifier.split(":");
        Modules modules = this;
        Module module = null;

        for (String name : names) {
            if (StringUtils.isEmpty(name)) {
                throw new PandaFrameworkException("Illegal name " + moduleQualifier);
            }

            Module nextModule = modules.get(name).getOrNull();

            if (nextModule == null && compute) {
                nextModule = new PandaModule(module, name);
                modules.include(nextModule);
            }

            if (nextModule == null) {
                return Option.none();
            }

            module = nextModule;
            modules = module;
        }

        return Option.of(module);
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
