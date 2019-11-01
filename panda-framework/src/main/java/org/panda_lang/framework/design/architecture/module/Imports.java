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

/**
 * Represents references imported in the specific space, e.g. file
 */
public interface Imports extends ModuleResource {

    /**
     * Import module using the given name
     *
     * @param name the name of module
     */
    void importModule(String name);

    /**
     * Import module
     *
     * @param module the module to import
     * // @return if prototype with the given name is already imported, the method will interrupt importing and return the name of that prototype
     */
    void importModule(Module module);

    /**
     * Import reference
     *
     * @param name the name of prototype to import as (may be different than prototype name)
     * @param reference the reference to prototype
     * @return if prototype with the given name is already imported, the method will return false, otherwise true
     */
    boolean importPrototype(String name, Reference reference);

    /**
     * Get the module loader used by current imports
     *
     * @return the module loader
     */
    ModuleLoader getModuleLoader();

}
