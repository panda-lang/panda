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

package org.panda_lang.utilities.commons.collection;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class Maps {

    /**
     * Update value of map
     *
     * @param map the map to use
     * @param key key of entry to update
     * @param defaultValue default value fetcher
     * @param updateFunction function used to fetch updated value
     * @param <K> type of key
     * @param <V> type of value
     */
    public static <K, V> void update(Map<K, V> map, K key, Supplier<V> defaultValue, Function<V, V> updateFunction) {
        V value = map.computeIfAbsent(key, currentKey -> defaultValue.get());
        map.put(key, updateFunction.apply(value));
    }

    /**
     * {@link java.util.Map#put(Object, Object)} but returns the used value
     *
     * @param map the map to put in
     * @param key the key
     * @param value the value
     * @param <K> type of key
     * @param <V> type of value
     * @return the object used as value
     */
    public static <K, V> V put(Map<? super K, ? super V> map, K key, V value) {
        map.put(key, value);
        return value;
    }

    /**
     * Swap keys with values
     *
     * @param map the map to swap
     * @param mapSupplier the supplier to get new map instance
     * @param <K> type of key
     * @param <V> type of value
     * @return new swapped map
     */
    public static <K, V> Map<V, K> swapped(Map<K, V> map, Supplier<Map<V, K>> mapSupplier) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getValue,
                        Map.Entry::getKey,
                        (key, value) -> { throw new IllegalStateException(String.format("Duplicate key %s", key)); },
                        mapSupplier
                ));
    }

    /**
     * Get mutable entry
     *
     * @param key key of entry
     * @param value value of entry
     * @param <K> type of key
     * @param <V> type of value
     * @return mutable entry
     */
    public static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    /**
     * Get immutable entry
     *
     * @param key key of entry
     * @param value value of entry
     * @param <K> type of key
     * @param <V> type of value
     * @return immutable entry
     */
    public static <K, V> Map.Entry<K, V> immutableEntryOf(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    /**
     * Create map based on vararg parameter
     *
     * @param values values
     * @param <K> type of key
     * @param <V> type of value
     * @return map based on the specified values
     */
    public static <K, V> Map<K, V> of(Object... values) {
        return of(null, null, values);
    }

    /**
     * Create map based on vararg parameter
     *
     * @param values values
     * @param keyType the class to define type of key
     * @param valueType the class to define type of value
     * @param <K> type of key
     * @param <V> type of value
     * @return map based on the specified values
     */
    @SuppressWarnings({ "unchecked", "unused" })
    public static <K, V> Map<K, V> of(Class<K> keyType, Class<V> valueType, Object... values) {
        if (values.length % 2 != 0) {
            throw new IllegalArgumentException("The number of given values is not even");
        }

        Map<K, V> map = new HashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            map.put((K) values[i], (V) values[i + 1]);
        }

        return map;
    }

}
