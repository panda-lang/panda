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
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PandaModulePath implements ModulePath {

    private final Map<String, Module> groups;

    public PandaModulePath() {
        this.groups = new HashMap<>();
        this.initialize();
    }

    private void initialize() {
        this.create((String) null);
    }

    @Override
    public Module create(String groupName) {
        if (groups.containsKey(groupName)) {
            return groups.get(groupName);
        }

        Module module = new PandaModule(groupName);
        groups.put(groupName, module);
        return module;
    }

    @Override
    public Module get(String groupName) {
        return this.getGroups().get(groupName);
    }

    @Override
    public Collection<? extends Module> getModules() {
        return this.groups.values();
    }

    public Map<String, Module> getGroups() {
        return groups;
    }

}
