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
import org.panda_lang.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.function.Option;

public interface TypeLoader {

    /**
     * Load type by this loader
     *
     * @param type the type to load
     * @return loaded type
     */
    Type load(Type type);

    /**
     * Find reference using the given name
     *
     * @param typeName the name to search for
     * @return the reference
     */
    Option<Type> forType(String typeName);

    /**
     * Find reference using associated java class
     *
     * @param
     */
    Option<Type> forJavaType(Class<?> javaClass);

    /**
     * Get type with the given name.
     * Use this method only if you are absolutely sure that the request type exists
     *
     * @param name the name to search for
     * @return the found type
     * @throws org.panda_lang.language.runtime.PandaRuntimeException if type does not exist
     */
    default Type requireType(String name) throws PandaRuntimeException {
        return forType(name).orThrow(() -> {
            throw new PandaRuntimeException("Cannot find type " + name);
        });
    }

    Option<Module> forModule(String moduleName);

}
