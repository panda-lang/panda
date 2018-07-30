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

import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleRegistry;

import java.util.*;

public class PandaModuleRegistry implements ModuleRegistry {

    private final Map<String, Module> groups;

    public PandaModuleRegistry() {
        this.groups = new HashMap<>();
        this.initialize();
    }

    private void initialize() {
        Module defaultModule = this.getOrCreate(null);
        groups.put("", defaultModule);
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
