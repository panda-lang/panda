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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PandaImports implements Imports {

    private final ModuleLoader loader;
    private final Map<String, Module> importedModules = new HashMap<>();
    private final Map<String, Reference> importedPrototypes = new HashMap<>();

    public PandaImports(ModuleLoader loader) {
        this.loader = loader;
    }

    public PandaImports(ModuleLoader loader, Module... modules) {
        this(loader);

        for (Module module : modules) {
            importModule(module);
        }
    }

    @Override
    public void importModule(String name) {
        Optional<Module> module = loader.getPath().get(name, loader);

        if (!module.isPresent()) {
            throw new PandaFrameworkException("Module " + name + " does not exist");
        }

        importedModules.put(name, loader.loadIfAbsent(module.get()));
    }

    @Override
    public void importModule(Module module) {
        importedModules.computeIfAbsent(module.getName(), name -> loader.loadIfAbsent(module));
    }

    @Override
    public boolean importPrototype(String name, Reference reference) {
        if (importedPrototypes.containsKey(name)) {
            return false;
        }

        importedPrototypes.put(name, reference);
        return true;
    }

    /**
     * Imports does not support this method
     *
     * @param associatedClass the class associated with prototype to search for
     * @return always empty optional
     */
    @Override
    public Optional<Reference> forClass(@Nullable Class<?> associatedClass) {
        throw new PandaRuntimeException("Not supported");
    }

    @Override
    public Optional<Reference> forName(CharSequence name) {
        Reference localPrototype = importedPrototypes.get(name.toString());

        if (localPrototype != null) {
            return Optional.of(localPrototype);
        }

        for (Module module : importedModules.values()) {
            Optional<Reference> reference = module.forName(name);

            if (reference.isPresent()) {
                return reference;
            }
        }

        return Optional.empty();
    }

    @Override
    public ModuleLoader getModuleLoader() {
        return loader;
    }

}
