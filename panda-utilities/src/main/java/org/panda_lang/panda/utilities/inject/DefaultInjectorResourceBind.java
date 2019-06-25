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

import java.util.Objects;
import java.util.function.BiFunction;

class DefaultInjectorResourceBind<T> implements InjectorResourceBind<T> {

    private final Class<T> associatedType;
    private InjectorResourceBindValue<T> value;

    DefaultInjectorResourceBind(Class<T> associatedType) {
        if (Objects.isNull(associatedType)) {
            throw new IllegalArgumentException("Associated type cannot be null at the same time");
        }

        this.associatedType = associatedType;
    }

    private <V> void with(InjectorResourceBindValue<T> value) {
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
    public void assignHandler(BiFunction<Class<?>, T, Object> handler) {
        with(new HandledInjectorResourceBindValue<>(handler));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getValue(Class<?> expected, Object bind) throws Exception {
        return value.getValue(expected, (T) bind);
    }

    @Override
    public Class<T> getAssociatedType() {
        return associatedType;
    }

}
