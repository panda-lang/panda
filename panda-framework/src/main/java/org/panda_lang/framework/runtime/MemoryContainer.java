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

package org.panda_lang.framework.runtime;

import org.jetbrains.annotations.Nullable;

/**
 * Represents segment of memory
 */
public interface MemoryContainer {

    /**
     * Put value in scope memory
     *
     * @param pointer index of the variable in current scope
     * @param value new value
     */
    @Nullable <T> T set(int pointer, @Nullable T value);

    /**
     * Get value at the given position
     *
     * @param pointer index of variable in current scope
     * @return the value at the given position
     */
    @Nullable <T> T get(int pointer);

    /**
     * @return the size of memory
     */
    int getMemorySize();

}
