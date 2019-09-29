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

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.LoadedModule;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class PandaModuleLoader implements ModuleLoader {

    private final ModulePath path;
    private final ModuleLoader parent;
    private final Map<String, LoadedModule> loadedModules = new HashMap<>(2);

    public PandaModuleLoader(ModuleLoader loader) {
        this(loader.getPath(), loader);
    }

    public PandaModuleLoader(ModulePath path) {
        this(path, null);
    }

    public PandaModuleLoader(ModulePath path, ModuleLoader parent) {
        this.path = path;
        this.parent = parent;
    }

    public LoadedModule load(String name) {
        return path.get(name)
                .map(this::loadIfAbsent)
                .orElseThrow((Supplier<? extends PandaFrameworkException>) () -> {
                    throw new PandaFrameworkException("Module '" + name + "' does not exist");
                });
    }

    @Override
    public LoadedModule loadIfAbsent(Module module) {
        //noinspection OptionalGetWithoutIsPresent
        return internalLoad(module, false).get();
    }

    @Override
    public boolean load(Module module) {
        return internalLoad(module, true).isPresent();
    }

    private Optional<LoadedModule> internalLoad(Module module, boolean emptyIfLoaded) {
        LoadedModule loadedModule = loadedModules.get(module.getName());

        if (loadedModule != null) {
            return emptyIfLoaded ? Optional.empty() : Optional.of(loadedModule);
        }

        loadedModule = new PandaLoadedModule(this, module);
        this.loadedModules.put(module.getName(), loadedModule);
        return Optional.of(loadedModule);
    }

    @Override
    public Optional<Reference> forClass(Class<?> associatedClass) {
        for (LoadedModule loadedModule : loadedModules.values()) {
            Optional<Reference> prototypeReference = loadedModule.forClass(associatedClass);

            if (prototypeReference.isPresent()) {
                return prototypeReference;
            }

        }
        return Optional.empty();
    }

    @Override
    public Optional<Reference> forName(CharSequence prototypeName) {
        String name = prototypeName.toString();
        String[] path = StringUtils.split(name, "::");

        if (path.length == 1) {
            throw new PandaRuntimeException("Cannot load prototype without module");
        }

        return load(path[0]).forName(path[1]);
    }

    @Override
    public Optional<LoadedModule> get(String name) {
        return Optional.ofNullable(loadedModules.get(name));
    }

    @Override
    public Optional<ModuleLoader> getParent() {
        return Optional.ofNullable(parent);
    }

    @Override
    public ModulePath getPath() {
        return path;
    }

}
