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

package org.panda_lang.panda.framework.language.architecture.module;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.architecture.module.LivingModule;
import org.panda_lang.panda.framework.design.architecture.module.Module;
import org.panda_lang.panda.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.panda.framework.design.architecture.module.ModulePath;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.array.PandaArray;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.utilities.commons.StreamUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PandaModuleLoader implements ModuleLoader {

    private final ModulePath path;
    private final Map<String, LivingModule> loadedModules;

    public PandaModuleLoader(ModulePath path) {
        this.path = path;
        this.loadedModules = new HashMap<>(2);
    }

    @Override
    public PandaModuleLoader include(Module module) {
        this.loadedModules.put(module.getName(), new PandaLivingModule(this, module));
        return this;
    }

    @Override
    public ModuleLoader include(ModulePath path, String name) {
        Optional<Module> module = path.get(name);

        if (!module.isPresent()) {
            throw new PandaFrameworkException("Module " + name + " does not exist in the provided path");
        }

        return this.include(module.get());
    }

    @Override
    public Optional<ClassPrototypeReference> forClass(String name) {
        if (StringUtils.isEmpty(name)) {
            return Optional.empty();
        }

        if (name.contains(":")) {
            throw new PandaRuntimeException("Not implemented");
        }

        if (name.endsWith(PandaArray.IDENTIFIER)) {
            return ArrayClassPrototypeUtils.obtain(this, name);
        }

        for (Module module : loadedModules.values()) {
            Optional<ClassPrototypeReference> reference = module.get(name);

            if (reference.isPresent()) {
                return reference;
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<LivingModule> get(String name) {
        return Optional.ofNullable(loadedModules.get(name));
    }

    @Override
    public Collection<String> names() {
        return StreamUtils.map(loadedModules.values(), Module::getName);
    }

    @Override
    public ModulePath getPath() {
        return path;
    }

}
