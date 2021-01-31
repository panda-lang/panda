/*
 * Copyright (c) 2021 dzikoysk
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

package org.panda_lang.framework.architecture.type.member;

import org.panda_lang.framework.architecture.type.Type;
import org.panda_lang.framework.architecture.type.Visibility;
import org.panda_lang.framework.interpreter.source.Location;

/**
 * Element of type
 */
public interface Metadata {

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
     * Get location of property
     *
     * @return the location
     */
    Location getLocation();

    /**
     * Get parent type
     *
     * @return the parent type
     */
    Type getType();

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
