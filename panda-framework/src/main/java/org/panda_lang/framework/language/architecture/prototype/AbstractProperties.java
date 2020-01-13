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

package org.panda_lang.framework.language.architecture.prototype;

import org.panda_lang.framework.design.architecture.prototype.ExecutableProperty;
import org.panda_lang.framework.design.architecture.prototype.Properties;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.utilities.commons.function.CachedSupplier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

abstract class AbstractProperties<T extends ExecutableProperty> implements Properties<T> {

    protected final Class<T> type;
    protected final Prototype prototype;
    protected final Map<String, Collection<CachedSupplier<T>>> propertiesMap = new HashMap<>();

    protected AbstractProperties(Class<T> type, Prototype prototype) {
        this.type = type;
        this.prototype = prototype;
    }

    @Override
    public void declare(String name, Supplier<T> propertySupplier) {
        Collection<CachedSupplier<T>> properties = propertiesMap.computeIfAbsent(name, methodsContainer -> new ArrayList<>());
        properties.add(new CachedSupplier<>(propertySupplier));
    }

    @Override
    public int size() {
        return propertiesMap.values().stream()
                .mapToInt(Collection::size)
                .sum();
    }

    @Override
    public boolean hasPropertyLike(String name) {
        if (propertiesMap.containsKey(name)) {
            return true;
        }

        for (Prototype base : prototype.getBases()) {
            Optional<Properties<T>> properties = base.getProperties(type);

            if (properties.isPresent() && properties.get().hasPropertyLike(name)) {
                return true;
            }
        }

        return false;
    }

    private List<T> withBases(List<T> properties, Function<Properties<? extends T>, Collection<? extends T>> mapper, Predicate<T> filter) {
        for (Prototype base : prototype.getBases()) {
            base.getProperties(type).ifPresent(baseProperties -> properties.addAll(mapper.apply(baseProperties).stream()
                    .filter(filter)
                    .collect(Collectors.toList())
            ));
        }

        return properties;
    }

    protected List<T> getPropertiesLike(String name, Predicate<T> filter) {
        List<T> properties = Optional.ofNullable(propertiesMap.get(name)).orElseGet(Collections::emptyList).stream()
                .map(CachedSupplier::get)
                .filter(filter)
                .collect(Collectors.toList());

        return withBases(properties, baseProperties -> baseProperties.getPropertiesLike(name), filter);
    }

    @Override
    public List<? extends T> getPropertiesLike(String name) {
        return getPropertiesLike(name, property -> true);
    }

    protected List<T> getProperties(Predicate<T> filter) {
        return withBases(new ArrayList<>(getDeclaredProperties(filter)), Properties::getProperties, filter);
    }

    @Override
    public List<? extends T> getProperties() {
        return getProperties(property -> true);
    }

    protected List<T> getDeclaredProperties(Predicate<T> filter) {
        return propertiesMap.values().stream()
                .flatMap(Collection::stream)
                .map(CachedSupplier::get)
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public List<? extends T> getDeclaredProperties() {
        return getDeclaredProperties(property -> true);
    }

    @Override
    public Prototype getPrototype() {
        return prototype;
    }

}
