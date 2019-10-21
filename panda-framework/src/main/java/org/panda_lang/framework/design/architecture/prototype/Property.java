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
 * Element of property
 */
public interface Property {

    /**
     * Get visibility of executable
     *
     * @return the visibility
     */
    Visibility getVisibility();

    /**
     * Get prototype
     *
     * @return the prototype
     */
    default Prototype getPrototype() {
        return getReference().fetch();
    }

    /**
     * Get associated prototype
     *
     * @return the reference to prototype
     */
    Reference getReference();

    /**
     * Get module of property
     *
     * @return the module
     */
    default Module getModule() {
        return getReference().getModule();
    }

    /**
     * Get location of property
     *
     * @return the location
     */
    SourceLocation getLocation();

    /**
     * Get name of prototype
     *
     * @return the name
     */
    String getName();

}
