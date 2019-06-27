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

package org.panda_lang.panda.utilities.inject;

import org.panda_lang.panda.utilities.commons.ObjectUtils;

import java.util.function.BiFunction;
import java.util.function.Supplier;

class DefaultInjectorResourceBind<T, V> implements InjectorResourceBind<T, V> {

    private final Class<?> associatedType;
    private final Class<?> dataType;
    private InjectorResourceBindValue<V> value;

    DefaultInjectorResourceBind(Class<?> associatedType) {
        this(associatedType, associatedType);
    }

    DefaultInjectorResourceBind(Class<?> associatedType, Class<?> dataType) {
        if (ObjectUtils.areNull(associatedType, dataType)) {
            throw new IllegalArgumentException("Associated type cannot be null at the same time");
        }

        this.associatedType = associatedType;
        this.dataType = dataType;
    }

    private void with(InjectorResourceBindValue<V> value) {
        this.value = value;
    }

    @Override
    public void assign(Class<?> type) {
        with(new TypeInjectorResourceBindValue<>(type));
    }

    @Override
    public void assignInstance(Object value) {
        with(new StaticInjectorResourceBindValue<>(value));
    }

    @Override
    public void assignInstance(Supplier<?> valueSupplier) {
        with(new StaticInjectorResourceBindValue<>(valueSupplier));
    }

    @Override
    public void assignHandler(BiFunction<Class<?>, V, ?> handler) {
        with(new HandledInjectorResourceBindValue<>(handler));
    }

    @Override
    public Object getValue(Class<?> expected, V data) throws Exception {
        return value.getValue(expected, data);
    }

    @Override
    public Class<?> getAssociatedType() {
        return associatedType;
    }

    @Override
    public Class<?> getDataType() {
        return dataType;
    }

}
