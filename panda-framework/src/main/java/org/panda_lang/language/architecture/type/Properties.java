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

package org.panda_lang.language.architecture.type;

import java.util.List;
import java.util.function.Supplier;

/**
 * Represents container of executable properties
 *
 * @param <T> generic type of represented properties
 */
public interface Properties<T extends ExecutableProperty> {

    /**
     * Declare a new property
     *
     * @param name the name of property
     * @param propertySupplier the property to add
     */
    void declare(String name, Supplier<T> propertySupplier);

    /**
     * Declare a new property
     *
     * @param property the property to add
     */
    default void declare(T property) {
        declare(property.getSimpleName(), () -> property);
    }

    /**
     * Get amount of properties
     *
     * @return the amount of properties
     */
    int size();

    /**
     * Check if container contains property with the given name
     *
     * @param name the name to search for
     * @return true if container contains property with the given name, otherwise false
     */
    boolean hasPropertyLike(String name);

    /**
     * Get properties with the given name
     *
     * @param name the name to search for
     * @return list of properties with the given name
     */
    List<? extends T> getPropertiesLike(String name);

    /**
     * Get properties declared in this container
     *
     * @return list of properties
     */
    List<? extends T> getDeclaredProperties();

    /**
     * Get all available properties
     *
     * @return the list of properties
     */
    List<? extends T> getProperties();

    /**
     * Get associated type
     *
     * @return the type
     */
    Type getType();

}
