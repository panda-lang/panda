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

import java.util.function.BiFunction;

final class HandledInjectorResourceBindValue<T> implements InjectorResourceBindValue<T> {

    private final BiFunction<Class<?>, T, ?> handler;

    HandledInjectorResourceBindValue(BiFunction<Class<?>, T, ?> handler) {
        this.handler = handler;
    }

    @Override
    public Object getValue(Class<?> expected, T bind) {
        return handler.apply(expected, bind);
    }

}
