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

import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.Reference;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * References container
 */
public interface ModuleResource {

    /**
     * Get prototype associated with the given class.
     * Use this method only if you are absolutely sure that the requested prototype exists
     *
     * @param associatedClass the associated class to search for
     * @return the found prototype
     * @throws org.panda_lang.framework.language.runtime.PandaRuntimeException if prototype does not exist
     */
    default Prototype requirePrototype(Class<?> associatedClass) throws PandaRuntimeException {
        return forClass(associatedClass)
                .map(Reference::fetch)
                .orElseThrow((Supplier<? extends PandaRuntimeException>) () -> {
                    throw new PandaRuntimeException("Cannot find prototype associated with " + associatedClass);
                });
    }

    /**
     * Get prototype with the given name.
     * Use this method only if you are absolutely sure that the request prototype exists
     *
     * @param name the name to search for
     * @return the found prototype
     * @throws org.panda_lang.framework.language.runtime.PandaRuntimeException if prototype does not exist
     */
    default Prototype requirePrototype(String name) throws PandaRuntimeException {
        return forName(name)
                .map(Reference::fetch)
                .orElseThrow((Supplier<? extends PandaRuntimeException>) () -> {
                    throw new PandaRuntimeException("Cannot find prototype " + name);
                });
    }

    /**
     * Find reference using the given class
     *
     * @param associatedClass the class associated with prototype to search for
     * @return the reference
     */
    Optional<Reference> forClass(Class<?> associatedClass);

    /**
     * Find reference using the given name
     *
     * @param prototypeName the name to search for
     * @return the reference
     */
    Optional<Reference> forName(CharSequence prototypeName);

}
