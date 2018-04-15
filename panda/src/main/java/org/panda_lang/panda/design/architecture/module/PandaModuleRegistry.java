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

package org.panda_lang.panda.design.architecture.module;

import org.apache.commons.lang3.StringUtils;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.language.interpreter.parsers.prototype.mapper.ClassPrototypeMappingManager;
import org.panda_lang.panda.utilities.commons.io.PackageUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaModuleRegistry implements ModuleRegistry {

    private final Map<String, Module> groups;
    private final ClassPrototypeMappingManager mappingManager;

    public PandaModuleRegistry() {
        this.groups = new HashMap<>();
        this.mappingManager = new ClassPrototypeMappingManager();
        this.initialize();
    }

    private void initialize() {
        Module defaultModule = this.getOrCreate(null);
        groups.put("", defaultModule);
    }

    @Override
    public ClassPrototype forClass(Class<?> clazz) {
        String name = PackageUtils.toString(clazz.getPackage(), "") + ":" + clazz.getSimpleName();
        ClassPrototype prototype = forName(name);

        if (prototype == null) {
            ClassPrototypeMappingManager mappingManager = new ClassPrototypeMappingManager();
            mappingManager.loadClass(clazz);
            Collection<ClassPrototype> generated = mappingManager.generate(this);

            if (generated == null) {
                return null;
            }
        }

        return forName(name);
    }

    @Override
    public ClassPrototype forName(String full) {
        String[] reference = full.split(":");

        if (reference.length == 0 || reference.length > 2) {
            return null;
        }

        Module module = (reference.length == 1) ? this.get(null) : this.get(reference[0]);

        if (module == null) {
            return null;
        }

        String className = (reference.length == 1) ? reference[0] : reference[1];

        if (StringUtils.isEmpty(className)) {
            return null;
        }

        return module.get(className);
    }

    @Override
    public Module getOrCreate(String groupName) {
        return this.getGroups().computeIfAbsent(groupName, PandaModule::new);
    }

    @Override
    public Module get(String groupName) {
        return this.getGroups().get(groupName);
    }

    @Override
    public Collection<Module> getModules() {
        return this.groups.values();
    }

    public Map<String, Module> getGroups() {
        return groups;
    }

}
