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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.ReferencesMap;
import org.panda_lang.framework.design.architecture.prototype.DynamicClass;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.utilities.commons.ClassUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

final class PandaReferencesMap extends HashMap<String, Reference> implements ReferencesMap {

    private final Map<DynamicClass, String> associatedClasses = new HashMap<>();

    @Override
    public boolean put(Reference reference) {
        if (associatedClasses.containsKey(reference.getAssociatedClass()) || containsKey(reference.getName())) {
            return false;
        }

        super.put(reference.getName(), reference);
        associatedClasses.put(reference.getAssociatedClass(), reference.getName());
        return true;
    }

    @Override
    public int countUsedPrototypes() {
        int sum = 0;

        for (Entry<String, Reference> entry : entrySet()) {
            if (entry.getValue().isInitialized()) {
                sum++;
            }
        }

        return sum;
    }

    @Override
    public Optional<Reference> forClass(Class<?> associatedClass) {
        String prototypeName = get(associatedClass);

        if (prototypeName == null) {
            if (associatedClass.isPrimitive()) {
                Class<?> primitiveClass = ClassUtils.getNonPrimitiveClass(associatedClass);

                if (primitiveClass != null) {
                    return forClass(primitiveClass);
                }
            }

            return Optional.empty();
        }

        return forName(prototypeName);
    }

    private @Nullable String get(Class<?> associatedClass) {
        for (Entry<DynamicClass, String> entry : associatedClasses.entrySet()) {
            if (entry.getKey().getImplementation().equals(associatedClass)) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public Optional<Reference> forName(CharSequence prototypeName) {
        return Optional.ofNullable(get(prototypeName.toString()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Entry<String, Reference>> getPrototypes() {
        Object sharedSet = entrySet(); // due to javac 1.8 bug
        return new HashSet<>((Collection<? extends Entry<String, Reference>>) sharedSet);
    }

}
