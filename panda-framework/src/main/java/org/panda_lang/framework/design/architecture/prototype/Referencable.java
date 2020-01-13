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

import org.panda_lang.framework.design.architecture.module.Module;

/**
 * Represent objects that can be reduced to the reference
 */
public interface Referencable {

    /**
     * Get Java class associated with the type
     *
     * @return the associated class
     */
    DynamicClass getAssociatedClass();

    /**
     * Get associated module
     *
     * @return the associated module
     */
    Module getModule();

    /**
     * Get simple name of property (without extra data)
     *
     * @return the name
     */
    String getSimpleName();

    /**
     * Get name of prototype
     *
     * @return the name
     */
    default String getName() {
        return getModule().getName() + "::" + getSimpleName();
    }

    /**
     * Fetch reference to prototype
     *
     * @return the reference
     */
    Reference toReference();

}
