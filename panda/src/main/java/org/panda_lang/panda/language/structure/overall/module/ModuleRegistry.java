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

import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.module.Module;
import org.panda_lang.panda.design.architecture.prototype.module.PandaModule;
import org.panda_lang.panda.design.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.prototype.mapper.ClassPrototypeMappingManager;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ModuleRegistry {

    private static final String DEFAULT_MODULE = StringUtils.EMPTY;

    private static final ModuleRegistry instance = new ModuleRegistry();
    private static final ClassPrototypeMappingManager mappingManager = new ClassPrototypeMappingManager();

    private final Map<String, Module> groups;
    private final Module defaultModule;

    public ModuleRegistry() {
        this.groups = new HashMap<>();
        this.defaultModule = new PandaModule(DEFAULT_MODULE);
        this.groups.put(DEFAULT_MODULE, this.defaultModule);
    }

    public int countPrototypes() {
        int count = 0;

        for (Module module : groups.values()) {
            count += module.getPrototypeCount();
        }

        return count;
    }

    public Module getOrCreate(String groupName) {
        return groups.computeIfAbsent(groupName, PandaModule::new);
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

    private static ClassPrototype mapClass(Class<?> clazz) {
        mappingManager.loadClass(clazz);
        Collection<ClassPrototype> prototypes = mappingManager.generate();

        if (prototypes == null || prototypes.size() == 0) {
            throw new PandaRuntimeException("Cannot map class " + clazz);
        }

        return prototypes.iterator().next();
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
            return mapClass(clazz);
        }

        ClassPrototype prototype = module.get(clazz.getSimpleName());

        if (prototype == null) {
            return mapClass(clazz);
        }

        return prototype;
    }

}
