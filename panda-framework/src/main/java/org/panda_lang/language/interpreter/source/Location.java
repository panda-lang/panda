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

package org.panda_lang.language.interpreter.source;

/**
 * Represents the location of element in a source
 */
public interface Location extends Localizable {

    /**
     * Represents unknown location
     */
    int UNKNOWN_LOCATION = -3;

    /**
     * Get indicated index
     *
     * @return the index
     */
    int getIndex();

    /**
     * Get readable indicated line
     *
     * @return the display line
     */
    default int getDisplayLine() {
        return getLine() + 1;
    }

    /**
     * Get indicated line
     *
     * @return the line
     */
    int getLine();

    /**
     * Get source info
     *
     * @return the source
     */
    Source getSource();

    @Override
    default Location toLocation() {
        return this;
    }
    
}
