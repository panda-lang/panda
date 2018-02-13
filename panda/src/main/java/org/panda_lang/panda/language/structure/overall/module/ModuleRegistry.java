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

package org.panda_lang.panda.language.structure.overall.module;

import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {

    private static final ModuleRegistry instance = new ModuleRegistry();
    private static final String DEFAULT_MODULE = StringUtils.EMPTY;

    private final Map<String, Module> groups;
    private final Module defaultModule;

    public ModuleRegistry() {
        this.groups = new HashMap<>();
        this.defaultModule = new Module(DEFAULT_MODULE);
        this.groups.put(DEFAULT_MODULE, this.defaultModule);
    }

    public Module getOrCreate(String groupName) {
        return groups.computeIfAbsent(groupName, Module::new);
    }

    public Module get(String groupName) {
        return groups.get(groupName);
    }

    public Module getDefaultModule() {
        return defaultModule;
    }

    public Map<String, Module> getGroups() {
        return groups;
    }

    public static ModuleRegistry getDefault() {
        return instance;
    }

    public static ClassPrototype forName(String prototypePath) {
        ModuleRegistry registry = getDefault();
        String[] parts = prototypePath.split(":");

        switch (parts.length) {
            case 1:
                return registry.getDefaultModule().get(parts[0]);
            case 2:
                Module module = registry.get(parts[0]);

                if (module == null) {
                    return null;
                }

                return module.get(parts[1]);
            default:
                throw new PandaRuntimeException("Invalid prototype path: " + prototypePath);
        }
    }

    public static ClassPrototype forClass(Class<?> clazz) {
        ModuleRegistry registry = ModuleRegistry.getDefault();

        String clazzPackage = clazz.getPackage() == null ? "" : clazz.getPackage().getName();
        Module module = registry.get(clazzPackage);

        if (module == null) {
            return null;
        }

        return module.get(clazz.getSimpleName());
    }

}
