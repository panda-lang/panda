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
import org.panda_lang.utilities.commons.function.CachedSupplier;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Custom implementation of map to store references with support for associated classes and {@link org.panda_lang.framework.design.architecture.module.ModuleResource}
 */
public interface ReferencesMap extends Map<String, CachedSupplier<Reference>>, ModuleResource {

    /**
     * Add reference to the map
     *
     * @param name the name of prototype
     * @param type the associated class
     * @param referenceSupplier the prototype supplier
     * @return false if name or type is already stored, otherwise true
     */
    boolean put(String name, Class<?> type, CachedSupplier<Reference> referenceSupplier);

    /**
     * Count used prototypes
     *
     * @return the amount of used prototypes
     */
    int countUsedPrototypes();

    /**
     * Get collection of entries that contains prototype references
     *
     * @return the collection of references
     */
    Collection<Entry<String, Supplier<Reference>>> getReferences();

}
