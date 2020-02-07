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

package org.panda_lang.framework.design.architecture.module;

import io.vavr.control.Option;
import org.panda_lang.framework.design.architecture.type.Referencable;
import org.panda_lang.framework.design.architecture.type.Reference;

import java.util.Collection;
import java.util.Map.Entry;

/**
 * Identifiable container of resources
 */
public interface Module extends Modules, ModuleResource {

    /**
     * Add reference to type to the module
     *
     * @param reference the reference to add
     * @return the added reference
     */
    Reference add(Referencable reference);

    /**
     * Check if the given module is submodule of the current module
     *
     * @param module the module to check
     * @return true if module is submodule, otherwise false
     */
    boolean isSubmodule(Module module);

    /**
     * Check if the module contains type associated with the specified class
     *
     * @param clazz the class to check
     * @return true if module contains type associated with the provided class
     */
    default boolean hasReference(Class<?> clazz) {
        return forClass(clazz).isDefined();
    }

    /**
     * Check if the module contains a reference to type with the given name
     *
     * @param name the name to search for
     * @return true if module contains such a reference
     */
    default boolean hasReference(CharSequence name) {
        return forName(name).isDefined();
    }

    /**
     * Get all types.
     * Iterable is used instead of collection because of the performance reasons.
     *
     * @return the iterable that contains all types
     */
    Collection<Entry<String, Reference>> getTypes();

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
    Option<Module> getParent();

    /**
     * Get name of module
     *
     * @return the name
     */
    String getName();

}
