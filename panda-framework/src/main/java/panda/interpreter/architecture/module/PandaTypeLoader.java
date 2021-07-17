/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.module;

import panda.interpreter.architecture.packages.Packages;
import panda.interpreter.architecture.type.Reference;
import panda.interpreter.architecture.type.Type;
import panda.std.Option;
import panda.std.stream.PandaStream;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PandaTypeLoader implements TypeLoader {

    private final Packages packages;
    private final Collection<TypeLoader> parents;
    private final Map<String, Type> loadedTypes = new HashMap<>(1024);
    private final Map<Class<?>, Type> associatedClasses = new HashMap<>(1024);

    public PandaTypeLoader(Packages packages, TypeLoader... parents) {
        this.packages = packages;
        this.parents = Arrays.asList(parents);
    }

    @Override
    public Type load(Type type) {
        loadedTypes.put(type.getName(), type);
        type.getAssociated().then(javaClass -> associatedClasses.put(javaClass, type));

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

    @Override
    public Option<Type> forJavaType(Class<?> javaClass) {
        return Option.of(associatedClasses.get(javaClass));
    }

    private Option<Type> forParentType(String type) {
        return PandaStream.of(parents)
                .mapOpt(typeResolver -> typeResolver.forType(type))
                .any();
    }

    private Option<Type> forPathType(String type) {
        String[] elements = type.split("::");

        if (elements.length != 2) {
            throw new IllegalArgumentException("Invalid qualifier name: " + type);
        }

        return packages.forModule(elements[0])
                .flatMap(module -> module.get(elements[1]))
                .map(Reference::fetchType);
    }

}
