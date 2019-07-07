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

package org.panda_lang.panda.framework.design.architecture.module;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Optional;

public interface Module {

    /**
     * Add a reference of prototype to the module
     *
     * @param reference the reference to add
     * @return the reference instance
     */
    ClassPrototypeReference add(ClassPrototypeReference reference);

    /**
     * Check if the module contains prototype associated with the specified class
     *
     * @param clazz the class to check
     * @return true if module contains reference associated with the provided class
     */
    default boolean hasPrototype(Class<?> clazz) {
        return this.hasPrototype(clazz.getCanonicalName());
    }

    /**
     * Check if the module contains prototype with the given name
     *
     * @param name the name to check
     * @return true if module contains that reference
     */
    default boolean hasPrototype(String name) {
        return get(name).isPresent();
    }

    /**
     * Get prototype associated with the given class
     *
     * @param clazz the class to search for
     * @return the reference associated with the class
     */
    default Optional<ClassPrototypeReference> getAssociatedWith(Class<?> clazz) {
        return StreamUtils.findFirst(getReferences(), reference -> reference.getAssociatedClass() == clazz);
    }

    /**
     * Get prototype by name
     *
     * @param className the name to search for
     * @return the reference with the given name
     */
    default Optional<ClassPrototypeReference> get(String className) {
        return StreamUtils.findFirst(getReferences(), reference -> className.equals(reference.getName()));
    }

    /**
     * Count initialized references
     *
     * @return the amount of used prototypes
     */
    default int getAmountOfUsedPrototypes() {
        return StreamUtils.count(getReferences(), ClassPrototypeReference::isInitialized);
    }

    /**
     * Count all references
     *
     * @return the amount of references
     */
    int getAmountOfReferences();

    /**
     * Get all references.
     * Iterable is used instead of collection because of the performance reasons.
     *
     * @return the iterable that contains all references
     */
    Iterable<ClassPrototypeReference> getReferences();

    /**
     * Get name of module
     *
     * @return the name
     */
    String getName();

}
