/*
 * Copyright (c) 2015-2020 Dzikoysk
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

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.type.Reference;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class PandaImports implements Imports {

    private final ModuleLoader loader;
    private final Map<String, Module> importedModules = new HashMap<>();
    private final Map<String, Reference> importedTypes = new HashMap<>();

    public PandaImports(ModuleLoader loader) {
        this.loader = loader;
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
    public boolean importType(String name, Reference reference) {
        if (importedTypes.containsKey(name)) {
            return false;
        }

        importedTypes.put(name, reference);
        return true;
    }

    /**
     * Imports does not support this method
     *
     * @param associatedClass the class associated with type to search for
     * @return always empty optional
     */
    @Override
    public Option<Reference> forClass(@Nullable Class<?> associatedClass) {
        throw new PandaRuntimeException("Not supported");
    }

    @Override
    public Option<Reference> forName(CharSequence name) {
        Reference localType = importedTypes.get(name.toString());

        if (localType != null) {
            return Option.of(localType);
        }

        for (Module module : importedModules.values()) {
            Option<Reference> reference = module.forName(name);

            if (reference.isDefined()) {
                return reference;
            }
        }

        return Option.none();
    }

    @Override
    public ModuleLoader getModuleLoader() {
        return loader;
    }

}
