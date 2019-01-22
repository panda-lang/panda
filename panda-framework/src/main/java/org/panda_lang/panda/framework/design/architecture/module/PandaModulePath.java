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

package org.panda_lang.panda.framework.design.architecture.module;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PandaModulePath implements ModulePath {

    public static final String DEFAULT_MODULE = null;

    private final Map<String, Module> modules;

    public PandaModulePath() {
        this.modules = new HashMap<>();
        this.initialize();
    }

    private void initialize() {
        this.create(null);
    }

    @Override
    public Module create(String name) {
        if (modules.containsKey(name)) {
            return modules.get(name);
        }

        Module module = new PandaModule(name);
        modules.put(name, module);
        return module;
    }

    @Override
    public ModulePath addModule(Module module) {
        modules.put(module.getName(), module);
        return this;
    }

    @Override
    public Optional<Module> get(String name) {
        return Optional.ofNullable(modules.get(name));
    }

    @Override
    public Collection<? extends Module> getModules() {
        return this.modules.values();
    }

}
