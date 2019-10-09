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

public interface Modules {

    /**
     * Add module
     *
     * @param module the module to add
     */
    void include(Module module);

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
     * @param moduleQualifier the name of module
     * @return the module
     */
    Optional<Module> get(String moduleQualifier);

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
