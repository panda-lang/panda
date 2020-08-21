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

package org.panda_lang.language.architecture.type.member;

import org.panda_lang.language.architecture.module.Module;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.architecture.type.Typed;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.interpreter.source.Location;

/**
 * Element of type
 */
public interface Property extends Typed {

    /**
     * Check if refers to the native context
     *
     * @return true if property is based on Java property
     */
    boolean isNative();

    /**
     * Get visibility of executable
     *
     * @return the visibility
     */
    Visibility getVisibility();

    /**
     * Get associated type
     *
     * @return the associated to type
     */
    Type getType();

    /**
     * Get module of property
     *
     * @return the module
     */
    default Module getModule() {
        return getType().getModule();
    }

    /**
     * Get location of property
     *
     * @return the location
     */
    Location getLocation();

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
    String getName();

}
