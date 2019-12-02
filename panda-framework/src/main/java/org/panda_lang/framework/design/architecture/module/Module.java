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

package org.panda_lang.framework.design.architecture.module;

import org.panda_lang.framework.design.architecture.prototype.Referencable;
import org.panda_lang.framework.design.architecture.prototype.Reference;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * Identifiable container of resources
 */
public interface Module extends Modules, ModuleResource {

    /**
     * Add reference to prototype to the module
     *
     * @param reference the reference to add
     * @return the added reference
     */
    Reference add(Referencable reference);

    /**
     * Count initialized Prototypes
     *
     * @return the amount of used prototypes
     */
    int countUsedPrototypes();

    /**
     * Count all Prototypes
     *
     * @return the amount of Prototypes
     */
    int countPrototypes();

    /**
     * Check if the given module is submodule of the current module
     *
     * @param module the module to check
     * @return true if module is submodule, otherwise false
     */
    boolean isSubmodule(Module module);

    /**
     * Check if the module contains prototype associated with the specified class
     *
     * @param clazz the class to check
     * @return true if module contains Prototype associated with the provided class
     */
    default boolean hasPrototype(Class<?> clazz) {
        return forClass(clazz).isPresent();
    }

    /**
     * Get all Prototypes.
     * Iterable is used instead of collection because of the performance reasons.
     *
     * @return the iterable that contains all Prototypes
     */
    Collection<Entry<String, Reference>> getPrototypes();

    /**
     * Get the loader used to load this module
     *
     * @return the module loader
     */
    ModuleLoader getModuleLoader();

    /**
     * Get parent module
     *
     * @return the parent module
     */
    Optional<Module> getParent();

    /**
     * Get name of module
     *
     * @return the name
     */
    String getName();

}
