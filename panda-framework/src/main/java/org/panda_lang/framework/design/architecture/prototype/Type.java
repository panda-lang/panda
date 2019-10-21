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

import org.panda_lang.framework.design.architecture.module.ModuleLoader;

import java.util.Collection;

/**
 * Basic set of data about type
 */
public interface Type extends Property {

    /**
     * Get reference to the array type of this type
     *
     * @param moduleLoader the loader to use
     * @return the reference to array type
     */
    Reference toArray(ModuleLoader moduleLoader);

    /**
     * Inherit the given reference
     *
     * @param reference the reference to inherit from
     */
    void addSuper(Reference reference);

    /**
     * Check if current declaration is assignable from the given declaration
     *
     * @param type to compare with
     * @return true if this type is assignable from the given declaration, otherwise false
     */
    boolean isAssignableFrom(Type type);

    /**
     * Get references to super types
     *
     * @return collection of supertypes
     */
    Collection<? extends Reference> getSupers();

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
