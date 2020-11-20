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

import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PandaTypeLoader implements TypeLoader {

    private final Collection<TypeLoader> parents;
    private final Map<String, Type> loadedTypes = new HashMap<>(1024);
    private final ModulePath modulePath;

    public PandaTypeLoader(ModulePath modulePath, TypeLoader... parents) {
        this.parents = Arrays.asList(parents);
        this.modulePath = modulePath;
    }

    @Override
    public Type load(Type type) {
        loadedTypes.put(type.getName(), type);

        if (!type.isInitialized()) {
            type.initialize(this);
        }

        return type;
    }

    @Override
    public Option<Type> forType(String type) {
        return Option.of(loadedTypes.get(type))
                .orElse(() -> forParentType(type))
                .orElse(() -> forPathType(type))
                .peek(this::load);
    }

    private Option<Type> forParentType(String type) {
        return PandaStream.of(parents)
                .mapOpt(typeResolver -> typeResolver.forType(type))
                .any();
    }

    private Option<Type> forPathType(String type) {
        return Option.of(type)
                .map(name -> name.split("::"))
                .filter(elements -> elements.length == 2)
                .map(elements -> new Pair<>(modulePath.forModule(elements[0]), elements[1]))
                .filter(pair -> pair.getKey().isDefined())
                .flatMap(pair -> pair.getKey().get().get(pair.getValue()))
                .map(reference -> reference.getType().get());
    }

    @Override
    public Option<Module> forModule(String moduleName) {
        return modulePath.forModule(moduleName);
    }

}
