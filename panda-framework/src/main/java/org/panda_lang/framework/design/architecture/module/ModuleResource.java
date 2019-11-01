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

import org.panda_lang.framework.design.architecture.prototype.Reference;

import java.util.Optional;

/**
 * References container
 */
public interface ModuleResource {

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
