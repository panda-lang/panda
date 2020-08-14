/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.language.architecture.module;

import org.panda_lang.language.architecture.type.Type;

public interface TypeLoader extends ModuleResource {

    /**
     * Load type by this loader
     *
     * @param type the type to load
     * @return loaded type
     */
    Type load(Type type);

    /**
     * Load java class as panda type
     *
     * @param module the associated with type module
     * @param type the class to load
     * @return loaded type
     */
    default Type load(Module module, Class<?> type) {
        return load(module, type, type.getSimpleName());
    }

    /**
     * Load java class as panda type
     *
     * @param module the associated with type module
     * @param type the class to load
     * @param alias the alias name for class type
     * @return loaded type
     */
    Type load(Module module, Class<?> type, String alias);

    /**
     * Load all types that belongs to the given module
     *
     * @param module the module to load
     */
    void load(Module module);

}
