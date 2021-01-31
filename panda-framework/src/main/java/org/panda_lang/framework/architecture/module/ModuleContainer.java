/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.module;

import org.panda_lang.utilities.commons.function.Completable;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.StreamUtils;

import java.util.Collection;

/**
 * Modules container
 */
public interface ModuleContainer {

    /**
     * Add module
     *
     * @param module the module to add
     */
    Module include(Module module);

    /**
     * Allocates module with the given qualifier or
     *
     * @return a module with the given name
     */
    Completable<Module> acquire(String moduleQualifier);

    /**
     * Get module with the given name
     *
     * @param moduleQualifier the name of module
     * @return the module
     */
    Completable<Option<Module>> forModule(String moduleQualifier);

    /**
     * Count used types
     *
     * @return the amount of used types
     */
    default long countUsedTypes() {
        return StreamUtils.sum(getModules(), Module::countUsedTypes);
    }

    /**
     * Count all available types
     *
     * @return the amount of types
     */
    default long countTypes() {
        return StreamUtils.sum(getModules(), Module::countTypes);
    }

    /**
     * Get all names of modules
     *
     * @return the all names
     */
    Collection<? extends String> getNames();

    /**
     * Get all submodules
     *
     * @return the collection of submodules
     */
    Collection<? extends Module> getModules();

}
