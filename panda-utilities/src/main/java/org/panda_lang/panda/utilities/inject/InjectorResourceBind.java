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

import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface InjectorResourceBind<T> extends Comparable<InjectorResourceBind> {

    void assign(Class<?> type);

    void assignInstance(Object value);

    void assignHandler(BiFunction<Class<?>, T, Object> handler);

    @Override
    default int compareTo(@NotNull InjectorResourceBind bind) {
        return Integer.compare(getType().priority, bind.getType().priority);
    }

    Object getValue(Class<?> expected, Object bind) throws Exception;

    Class<T> getAssociatedType();

    BindType getType();

    enum BindType {

        ANNOTATION(1),
        TYPE(0);

        private final int priority;

        BindType(int priority) {
            this.priority = priority;
        }

    }

}
