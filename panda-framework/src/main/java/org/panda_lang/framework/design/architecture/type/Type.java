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

package org.panda_lang.framework.design.architecture.type;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;

import java.util.Collection;
import java.util.Optional;

/**
 * Extensible owner of properties
 */
public interface Type extends Property, Referencable {

    /**
     * Get type to the array type of this type
     *
     * @param moduleLoader the loader to use
     * @return the array type
     */
    Type toArray(ModuleLoader moduleLoader);

    /**
     * Inherit the given type
     *
     * @param baseType the type to inherit from
     */
    void addBase(Type baseType);

    /**
     * Support automatic casting to other type
     *
     * @param to the type to cast to
     * @param autocast the autocast
     */
    void addAutocast(Type to, Autocast<?, ?> autocast);

    /**
     * Check if current declaration is assignable from the given declaration
     *
     * @param type to compare with
     * @return true if this type is assignable from the given declaration, otherwise false
     */
    boolean isAssignableFrom(@Nullable Type type);

    /**
     * Check if the type represents array type
     *
     * @return true if the type represents array type, otherwise false
     */
    boolean isArray();

    /**
     * Get methods that belongs to the type
     *
     * @return the methods
     */
    Methods getMethods();

    /**
     * Get fields that belongs to the type
     *
     * @return the fields
     */
    Fields getFields();

    /**
     * Get constructors that belongs to the type
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
     * Get autocast for the given type
     *
     * @param to the type to search for
     * @return the autocast
     */
    Optional<Autocast<?, ?>> getAutocast(Type to);

    /**
     * Get supertypes of type
     *
     * @return collection of supertypes
     */
    Collection<? extends Type> getBases();

    /**
     * Get state of type
     *
     * @return the state
     */
    State getState();

    /**
     * Get represented model of type
     *
     * @return the model that represents type
     */
    String getModel();

    /**
     * Get associated module
     *
     * @return the associated module
     */
    @Override
    Module getModule();

    @Override
    default String getName() {
        return getModule().getName() + "::" + getSimpleName();
    }

}
