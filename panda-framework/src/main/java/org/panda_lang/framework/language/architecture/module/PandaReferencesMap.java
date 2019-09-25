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

import org.panda_lang.framework.design.architecture.module.ReferencesMap;
import org.panda_lang.framework.design.architecture.prototype.PrototypeReference;
import org.panda_lang.utilities.commons.function.CachedSupplier;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

final class PandaReferencesMap extends HashMap<String, CachedSupplier<PrototypeReference>> implements ReferencesMap {

    private final Map<Class<?>, String> associatedClasses = new HashMap<>();

    @Override
    public boolean put(String name, Class<?> type, CachedSupplier<PrototypeReference> referenceSupplier) {
        if (associatedClasses.containsKey(type)) {
            return false;
        }

        super.put(name, referenceSupplier);
        associatedClasses.put(type, name);
        return true;
    }

    @Override
    public int countUsedPrototypes() {
        int sum = 0;

        for (Entry<String, CachedSupplier<PrototypeReference>> reference : entrySet()) {
            if (reference.getValue().isInitialized() && reference.getValue().get().isInitialized()) {
                sum++;
            }
        }

        return sum;
    }

    @Override
    public Optional<PrototypeReference> forClass(Class<?> associatedClass) {
        String prototypeName = associatedClasses.get(associatedClass);

        if (prototypeName == null) {
            return Optional.empty();
        }

        return forName(prototypeName);
    }

    @Override
    public Optional<PrototypeReference> forName(CharSequence prototypeName) {
        Supplier<PrototypeReference> prototypeReferenceSupplier = get(prototypeName.toString());

        if (prototypeReferenceSupplier == null) {
            return Optional.empty();
        }

        return Optional.of(prototypeReferenceSupplier.get());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Entry<String, Supplier<PrototypeReference>>> getReferences() {
        Object sharedSet = entrySet(); // due to javac 1.8 bug
        return new HashSet<>((Collection<? extends Entry<String, Supplier<PrototypeReference>>>) sharedSet);
    }

}
