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

import org.panda_lang.framework.design.architecture.module.Module;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;

/**
 * Element of prototype
 */
public interface Property extends Typed {

    /**
     * Get visibility of executable
     *
     * @return the visibility
     */
    Visibility getVisibility();

    /**
     * Get associated prototype
     *
     * @return the associated to prototype
     */
    Prototype getPrototype();

    /**
     * Get module of property
     *
     * @return the module
     */
    default Module getModule() {
        return getPrototype().getModule();
    }

    /**
     * Get location of property
     *
     * @return the location
     */
    SourceLocation getLocation();

    /**
     * Get simple name of property (without extra data)
     *
     * @return the name
     */
    String getSimpleName();

    /**
     * Get property name
     *
     * @return the name of property
     */
    default String getPropertyName() {
        return getSimpleName();
    }

}
