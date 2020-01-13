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

import java.util.Optional;

/**
 * ModuleLoader stores all imported modules wrapped into {@link org.panda_lang.framework.design.architecture.module.Module}
 */
public interface ModuleLoader extends ModuleResource {

    /**
     * Include module
     *
     * @param module the module to include
     * @return the loader instance
     */
    boolean load(Module module);

    /**
     * Get loaded module of the given module or load if not loaded
     *
     * @param module the module to search for
     * @return loaded module
     */
    Module loadIfAbsent(Module module);

    /**
     * Get loaded module with given name
     *
     * @param name the name to search for
     * @return the loaded module, otherwise empty optional
     */
    Optional<Module> get(String name);

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
