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
import org.panda_lang.language.architecture.type.Signed;
import org.panda_lang.language.architecture.type.Visibility;
import org.panda_lang.language.interpreter.source.Location;

/**
 * Element of type
 */
public interface Metadata extends Signed {

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
     * Get module of property
     *
     * @return the module
     */
    default Module getModule() {
        return getSignature().getType().getModule();
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
