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
import java.util.function.Supplier;

public interface InjectorResourceBind<T, V> extends Comparable<InjectorResourceBind> {

    /**
     * Assign class to the bind (a new instance will be created each time)
     *
     * @param type the type of value to assign
     */
    void assign(Class<?> type);

    /**
     * Assign object to the bind
     *
     * @param value the instance to assign
     */
    void assignInstance(Object value);

    /**
     * Assign value supplier to the bind
     *
     * @param valueSupplier the supplier to assign
     */
    void assignInstance(Supplier<?> valueSupplier);

    /**
     * Assign custom handler to the bind
     *
     * @param handler the handler which accepts type of parameter and bind type as arguments
     */
    void assignHandler(BiFunction<Class<?>, V, ?> handler);

    /**
     * Get value of bind for the expected (parameter) type and instance of bind type
     *
     * @param expected the expected return type
     * @param data instance of bind generic type
     * @return the result value
     * @throws Exception if anything wrong will happen, whole process should be stopped
     */
    Object getValue(Class<?> expected, V data) throws Exception;

    /**
     * Get associated type with the bind
     *
     * @return the associated type
     */
    Class<?> getAssociatedType();

    /**
     * Get data type
     *
     * @return the data type
     */
    Class<?> getDataType();

    @Override
    default int compareTo(@NotNull InjectorResourceBind bind) {
        return Integer.compare(InjectorResourceBindType.of(getAssociatedType()).getPriority(), InjectorResourceBindType.of(bind.getAssociatedType()).getPriority());
    }

}
