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
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.function.Supplier;

/**
 * References container
 */
public interface ModuleResource {

    /**
     * Get type associated with the given class.
     * Use this method only if you are absolutely sure that the requested type exists
     *
     * @param associatedClass the associated class to search for
     * @return the found type
     * @throws org.panda_lang.framework.language.runtime.PandaRuntimeException if type does not exist
     */
    default Type requireType(Class<?> associatedClass) throws PandaRuntimeException {
        return forClass(associatedClass).getOrElseThrow((Supplier<? extends PandaRuntimeException>) () -> {
            throw new PandaRuntimeException("Cannot find type associated with " + associatedClass);
        });
    }

    /**
     * Get type with the given name.
     * Use this method only if you are absolutely sure that the request type exists
     *
     * @param name the name to search for
     * @return the found type
     * @throws org.panda_lang.framework.language.runtime.PandaRuntimeException if type does not exist
     */
    default Type requireType(String name) throws PandaRuntimeException {
        return forName(name).getOrElseThrow((Supplier<? extends PandaRuntimeException>) () -> {
            throw new PandaRuntimeException("Cannot find type " + name);
        });
    }

    /**
     * Find reference using the given class
     *
     * @param associatedClass the class associated with type to search for
     * @return the reference
     */
    Option<Type> forClass(Class<?> associatedClass);

    /**
     * Find reference using the given name
     *
     * @param typeName the name to search for
     * @return the reference
     */
    Option<Type> forName(CharSequence typeName);

}
