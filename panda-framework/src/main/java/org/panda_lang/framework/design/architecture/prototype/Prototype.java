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

package org.panda_lang.framework.design.architecture.prototype;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;

import java.util.Collection;
import java.util.Optional;

/**
 * Basic set of static data about a type
 */
public interface Prototype extends Property, Referencable {

    /**
     * Get prototype to the array type of this type
     *
     * @param moduleLoader the loader to use
     * @return the array prototype
     */
    Prototype toArray(ModuleLoader moduleLoader);

    /**
     * Inherit the given prototype
     *
     * @param basePrototype the prototype to inherit from
     */
    void addBase(Prototype basePrototype);

    /**
     * Check if current declaration is assignable from the given declaration
     *
     * @param prototype to compare with
     * @return true if this type is assignable from the given declaration, otherwise false
     */
    boolean isAssignableFrom(@Nullable Prototype prototype);

    /**
     * Check if the prototype represents array type
     *
     * @return true if the prototype represents array type, otherwise false
     */
    boolean isArray();

    /**
     * Get methods that belongs to the prototype
     *
     * @return the methods
     */
    Methods getMethods();

    /**
     * Get fields that belongs to the prototype
     *
     * @return the fields
     */
    Fields getFields();

    /**
     * Get constructors that belongs to the prototype
     *
     * @return the constructors
     */
    Constructors getConstructors();

    /**
     * Get properties of the given type
     *
     * @param propertyType the property class
     * @param <T> generic type that represents the property type
     * @return the properties
     */
    <T extends ExecutableProperty> Optional<Properties<T>> getProperties(Class<T> propertyType);

    /**
     * Get supertypes of prototype
     *
     * @return collection of supertypes
     */
    Collection<? extends Prototype> getBases();

    /**
     * Get state of type
     *
     * @return the state
     */
    State getState();

    /**
     * Get type of prototype
     *
     * @return the prototype type
     */
    String getType();

    /**
     * Get Java class associated with the type
     *
     * @return the associated class
     */
    Class<?> getAssociatedClass();

}
