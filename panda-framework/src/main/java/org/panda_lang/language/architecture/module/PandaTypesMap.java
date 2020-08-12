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

import org.panda_lang.language.architecture.type.DynamicClass;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.ClassUtils;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.PandaStream;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

final class PandaTypesMap extends HashMap<String, Type> implements TypesMap {

    private final Map<DynamicClass, String> associatedClasses = new HashMap<>();

    @Override
    public boolean put(Type type) {
        if (associatedClasses.containsKey(type.getAssociatedClass()) || containsKey(type.getSimpleName())) {
            return false;
        }

        super.put(type.getSimpleName(), type);
        associatedClasses.put(type.getAssociatedClass(), type.getSimpleName());
        return true;
    }

    @Override
    public long countUsedTypes() {
        return PandaStream.of(entrySet()).count(entry -> entry.getValue().isInitialized());
    }

    @Override
    public Option<Type> forClass(Class<?> associatedClass) {
        return get(associatedClass)
                .flatMap(this::forName)
                .orElse(() -> associatedClass.isPrimitive() ? forClass(ClassUtils.getNonPrimitiveClass(associatedClass)) : Option.none());
    }

    @Override
    public Option<Type> forName(CharSequence typeName) {
        return Option.of(get(typeName.toString()));
    }

    private Option<String> get(Class<?> associatedClass) {
        return PandaStream.of(associatedClasses.entrySet())
                .find(entry -> entry.getKey().fetchStructure().equals(associatedClass))
                .map(Entry::getValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Entry<String, Type>> getTypes() {
        Object sharedSet = entrySet(); // due to javac 1.8 bug
        return new HashSet<>((Collection<? extends Entry<String, Type>>) sharedSet);
    }

}
