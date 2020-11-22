/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.module;

import org.panda_lang.language.PandaFrameworkException;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents references imported in the specific space, e.g. file
 */
public final class Imports {

    private final TypeLoader typeLoader;
    private final Map<String, Module> importedModules = new HashMap<>();
    private final Map<String, Reference> importedTypes = new HashMap<>();

    public Imports(TypeLoader typeLoader) {
        this.typeLoader = typeLoader;
    }

    /**
     * Import module using the given name
     *
     * @param name the name of module
     */

    public void importModule(String name) {
        typeLoader.forModule(name)
                .peek(module -> importedModules.put(name, module))
                .orThrow(() -> {
                    throw new PandaFrameworkException("Module " + name + " does not exist");
                });
    }

    /**
     * Import module
     *
     * @param module the module to import
     * // @return if type with the given name is already imported, the method will interrupt importing and return the name of that type
     */
    public void importModule(Module module) {
        importedModules.putIfAbsent(module.getName(), module);
    }

    public boolean importType(Reference reference) {
        return importType(reference.getSimpleName(), reference);
    }

    /**
     * Import reference
     *
     * @param name the name of type to import as (may be different than type name)
     * @param reference the reference to type
     * @return if type with the given name is already imported, the method will return false, otherwise true
     */
    public boolean importType(String name, Reference reference) {
        reference.getType().thenApply(typeLoader::load);

        if (importedTypes.containsKey(name)) {
            return false;
        }

        importedTypes.put(name, reference);
        return true;
    }

    public Option<Reference> forType(String name) {
        return Option.of(importedTypes.get(name)).orElse(() -> forModuleType(name));
    }

    public Option<Reference> forModuleType(String name) {
        List<? extends Reference> references = PandaStream.of(importedModules.entrySet())
                .mapOpt(entry -> entry.getValue().get(name))
                .collect(Collectors.toList());

        if (references.size() > 1) {
            throw new PandaFrameworkException("Duplicated reference names: " + references);
        }

        return references.isEmpty() ? Option.none() : Option.of(references.get(0));
    }

    /**
     * Get associated type loader
     *
     * @return the associated type loader
     */
    public TypeLoader getTypeLoader() {
        return typeLoader;
    }

}
