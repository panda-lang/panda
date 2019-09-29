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

import org.panda_lang.framework.design.architecture.prototype.Reference;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;

public interface Module extends ModuleResource {

    /**
     * Add a reference of prototype to the module
     *
     * @param referenceSupplier the reference to add
     */
    void add(String name, Class<?> associatedClass, Supplier<Reference> referenceSupplier);

    /**
     * Add submodule to the module
     *
     * @param submodule the submodule to add
     */
    void addSubmodule(Module submodule);

    /**
     * Count initialized references
     *
     * @return the amount of used prototypes
     */
    int countUsedPrototypes();

    /**
     * Count all references
     *
     * @return the amount of references
     */
    int countReferences();

    /**
     * Check if the module contains prototype associated with the specified class
     *
     * @param clazz the class to check
     * @return true if module contains reference associated with the provided class
     */
    default boolean hasPrototype(Class<?> clazz) {
        return forName(clazz.getSimpleName()).isPresent();
    }

    /**
     * Get all references.
     * Iterable is used instead of collection because of the performance reasons.
     *
     * @return the iterable that contains all references
     */
    Collection<Entry<String, Supplier<Reference>>> getReferences();

    /**
     * Get all submodules
     *
     * @return the collection of submodules
     */
    Collection<? extends Module> getSubmodules();

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
