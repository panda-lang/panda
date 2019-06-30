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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserException;
import org.panda_lang.panda.utilities.commons.StreamUtils;

import java.util.Collection;
import java.util.Optional;

/**
 *
 * ModulePath is collection of all available modules. Content:
 *
 * <ul>
 *     <li>default module = language core prototypes like numbers, strings, object</li>
 *     <li>all available modules</li>
 * </ul>
 *
 */
public interface ModulePath {

    /**
     * Constant that represents default module. The value of constant is null,
     * because default module does not have a name.
     */
    String DEFAULT_MODULE = null;

    /**
     * Add module to the path
     *
     * @param module the module to add
     * @return the path instance
     */
    Module include(Module module);

    /**
     * Check if the path contains module with the given name
     *
     * @param name the name to check
     * @return true if path contains that module
     */
    boolean hasModule(@Nullable String name);

    /**
     * Count used prototypes
     *
     * @return the amount of used prototypes
     */
    default int getAmountOfUsedPrototypes() {
        return StreamUtils.sum(getModules(), Module::getAmountOfUsedPrototypes);
    }

    /**
     * Count all available references
     *
     * @return the amount of references
     */
    default int getAmountOfReferences() {
        return StreamUtils.sum(getModules(), Module::getAmountOfReferences);
    }

    /**
     * Get default module
     *
     * @return the default module
     *
     * @see org.panda_lang.panda.framework.design.architecture.module.ModulePath#DEFAULT_MODULE
     */
    default Module getDefaultModule() {
        return get(DEFAULT_MODULE).orElseThrow(() -> new PandaParserException(getClass() + " does not have default module"));
    }

    /**
     * Get module
     *
     * @param name the name of module
     * @return the module
     */
    Optional<Module> get(@Nullable String name);

    /**
     * Get all modules
     *
     * @return the collection of available modules
     */
    Collection<? extends Module> getModules();

}
