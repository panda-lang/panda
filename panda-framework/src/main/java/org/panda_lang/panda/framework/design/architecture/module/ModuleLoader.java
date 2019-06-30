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

import java.util.Collection;
import java.util.Optional;

/**
 * ModuleLoader stores all imported modules wrapped into {@link org.panda_lang.panda.framework.design.architecture.module.LivingModule}. Content:
 *
 * <ul>
 *     <li>creates local module - associated with loader module for local imports</li>
 *     <li>includes default module by default into local module</li>
 *     <li>keeps imported modules as living modules</li>
 * </ul>
 *
 */
public interface ModuleLoader {

    /**
     * Include module
     *
     * @param module the module to include
     * @return the loader instance
     */
    ModuleLoader load(Module module);

    /**
     * Get reference using the given name
     *
     * @param name the name to search
     * @return the found (or not) prototype reference wrapped into optional
     */
    Optional<ClassPrototypeReference> forName(String name);

    /**
     * Get living module using the given name
     *
     * @param name the name to search for
     * @return the found (or not) living module
     */
    Optional<LivingModule> get(String name);

    /**
     * Collect all names of imported modules
     *
     * @return the collection of names
     */
    Collection<String> getNames();

    /**
     * Get local module. The local module contains loaded {@link ModulePath#getDefaultModule()} by default,
     * it's also intended for non-exposed module metadata like local imports.
     *
     * @return the default module
     *
     * @see org.panda_lang.panda.framework.design.architecture.module.ModulePath#DEFAULT_MODULE
     */
    LivingModule getLocalModule();

    /**
     * Get parent loader
     *
     * @return the parent loader
     */
    Optional<ModuleLoader> getParent();

    /**
     * Get the path used by loader
     *
     * @return the path
     */
    ModulePath getPath();

}
