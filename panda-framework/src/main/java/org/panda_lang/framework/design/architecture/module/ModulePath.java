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

import org.panda_lang.utilities.commons.StreamUtils;

import java.util.Collection;
import java.util.Optional;

/**
 * ModulePath is collection of all available modules
 */
public interface ModulePath {

    /**
     * Add module to the path
     *
     * @param module the module to add
     * @return the module instance
     */
    Module include(Module module);

    /**
     * Count used prototypes
     *
     * @return the amount of used prototypes
     */
    default int countUsedPrototypes() {
        return StreamUtils.sum(getModules(), Module::countUsedPrototypes);
    }

    /**
     * Count all available references
     *
     * @return the amount of references
     */
    default int countReferences() {
        return StreamUtils.sum(getModules(), Module::countReferences);
    }

    /**
     * Get module with the given name
     *
     * @param name the name of module
     * @return the module
     */
    Optional<Module> get(String name);

    /**
     * Get all modules
     *
     * @return the collection of available modules
     */
    Collection<? extends Module> getModules();

}
