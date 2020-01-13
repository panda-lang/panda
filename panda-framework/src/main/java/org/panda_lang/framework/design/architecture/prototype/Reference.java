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

package org.panda_lang.framework.design.architecture.prototype;

import java.util.function.Consumer;

/**
 * Reference to prototype
 */
public interface Reference extends Referencable {

    /**
     * Get assigned prototype
     *
     * @return the prototype
     * @throws org.panda_lang.framework.design.architecture.prototype.ReferenceFetchException if anything happen
     */
    Prototype fetch() throws ReferenceFetchException;

    /**
     * Add initializer to the fetcher. If prototype is already initialized, the initializer is executed immediately
     *
     * @param initializer the initializer to add
     * @return current reference
     */
    Reference addInitializer(Consumer<Prototype> initializer);

    /**
     * Check if the reference is already initialized
     *
     * @return true if initialized, otherwise false
     */
    boolean isInitialized();

    /**
     * Count registered initializers
     *
     * @return amount of initializers
     */
    int getAmountOfInitializers();

    @Override
    default Reference toReference() {
        return this;
    }

}
