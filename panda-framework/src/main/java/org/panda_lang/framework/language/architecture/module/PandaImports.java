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
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.LoadedModule;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class PandaImports implements Imports {

    private final ModuleLoader loader;
    private final Map<String, LoadedModule> importedModules = new HashMap<>();
    private final Map<String, Supplier<Reference>> importedReferences = new HashMap<>();

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
    public void importModule(Module module) {
        importedModules.computeIfAbsent(module.getName(), name -> loader.loadIfAbsent(module));
    }

    @Override
    public boolean importReference(String name, Supplier<Reference> supplier) {
        if (importedReferences.containsKey(name)) {
            return false;
        }

        importedReferences.put(name, supplier);
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
        Supplier<Reference> localReference = importedReferences.get(name.toString());

        if (localReference != null) {
            return Optional.of(localReference.get());
        }

        for (LoadedModule value : importedModules.values()) {
            Optional<Reference> reference = value.forName(name);

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