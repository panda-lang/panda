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

package org.panda_lang.framework.design.architecture.prototype;

/**
 * The reference to prototype
 */
public interface Reference extends Type {

    /**
     * Get assigned prototype
     *
     * @return the prototype
     */
    Prototype fetch();

    /**
     * Add initializer to the fetcher. If reference is already initialized, the initializer is executed immediately
     *
     * @param initializer the initializer to add
     * @return current reference
     */
    Reference addInitializer(Runnable initializer);

    /**
     * Check if reference is already initialized
     *
     * @return true if initialized, otherwise false
     */
    boolean isInitialized();

}
