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

package org.panda_lang.framework.language.architecture.module;

import io.vavr.control.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.module.Imports;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModulePath;
import org.panda_lang.framework.design.architecture.module.TypeLoader;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.language.architecture.type.array.ArrayClassTypeFetcher;
import org.panda_lang.framework.language.architecture.type.array.PandaArray;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.HashMap;
import java.util.Map;

public final class PandaImports implements Imports {

    private final ModulePath modulePath;
    private final TypeLoader typeLoader;
    private final Map<String, Module> importedModules = new HashMap<>();
    private final Map<String, Type> importedTypes = new HashMap<>();

    public PandaImports(ModulePath modulePath, TypeLoader typeLoader) {
        this.modulePath = modulePath;
        this.typeLoader = typeLoader;
    }

    @Override
    public void importModule(String name) {
        modulePath.get(name)
                .peek(module -> {
                    typeLoader.load(module);
                    importedModules.put(name, module);
                })
                .onEmpty(() -> {
                    throw new PandaFrameworkException("Module " + name + " does not exist");
                });
    }

    @Override
    public void importModule(Module module) {
        importedModules.putIfAbsent(module.getName(), module);
    }

    @Override
    public boolean importType(String name, Type type) {
        type = typeLoader.load(type);

        if (importedTypes.containsKey(name)) {
            return false;
        }

        importedTypes.put(name, type);
        return true;
    }

    /**
     * Imports does not support this method
     *
     * @param associatedClass the class associated with type to search for
     * @return always empty optional
     */
    @Override
    public Option<Type> forClass(@Nullable Class<?> associatedClass) {
        throw new PandaRuntimeException("Not supported");
    }

    @Override
    public Option<Type> forName(CharSequence name) {
        if (name.toString().endsWith(PandaArray.IDENTIFIER)) {
            return ArrayClassTypeFetcher.fetch(typeLoader, name.toString());
        }

        return Option.of(importedTypes.get(name.toString()))
                .orElse(() -> ModuleResourceUtils.forName(importedModules.values(), name))
                .peek(typeLoader::load);
    }

    @Override
    public TypeLoader getTypeLoader() {
        return typeLoader;
    }

}
