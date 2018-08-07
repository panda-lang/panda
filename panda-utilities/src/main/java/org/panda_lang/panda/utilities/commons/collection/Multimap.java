/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.commons.collection;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public class Multimap<K, V> implements Serializable {

    private final Map<K, Collection<V>> map;
    private final Supplier<Collection<V>> collectionSupplier;

    public Multimap(Map<K, Collection<V>> map, Supplier<Collection<V>> collectionSupplier) {
        this.map = map;
        this.collectionSupplier = collectionSupplier;
    }

    public void put(K key, V value) {
        Collection<V> collection = map.computeIfAbsent(key, k -> collectionSupplier.get());
        collection.add(value);
    }

    public void put(K key, Collection<V> values) {
        Collection<V> collection = map.computeIfAbsent(key, k -> collectionSupplier.get());
        collection.addAll(values);
    }

    public void clear() {
        map.clear();
    }

    public @Nullable Collection<V> get(K key) {
        return map.get(key);
    }

}
