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

import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.array.ArrayClassTypeFetcher;
import org.panda_lang.language.architecture.type.array.PandaArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class PandaTypeLoader implements TypeLoader {

    private final Collection<TypeLoader> parents;
    private final TypesMap loadedTypes = new PandaTypesMap();
    private final Collection<Module> loadedModules = new ArrayList<>();

    public PandaTypeLoader(TypeLoader... parents) {
        this.parents = Arrays.asList(parents);
    }

    @Override
    public Type load(Type type) {
        if (!type.isInitialized()) {
            type.initialize(this);
        }

        return type;
    }

    @Override
    public void load(Module module) {
        loadedModules.add(module);
    }

    @Override
    public Option<Type> forClass(Class<?> associatedClass) {
        return loadedTypes.forClass(associatedClass)
                .orElse(() -> ModuleResourceUtils.forClass(loadedModules, associatedClass))
                .orElse(() -> ModuleResourceUtils.forClass(parents, associatedClass))
                .peek(this::load);
    }

    @Override
    public Option<Type> forName(CharSequence typeName) {
        if (typeName.toString().endsWith(PandaArray.IDENTIFIER)) {
            return ArrayClassTypeFetcher.fetch(this, typeName.toString());
        }

        return loadedTypes.forName(typeName)
                .orElse(() -> ModuleResourceUtils.forName(loadedModules, typeName))
                .orElse(() -> ModuleResourceUtils.forName(parents, typeName))
                .peek(this::load);
    }

}
