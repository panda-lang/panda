/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.design.runtime.memory;

public interface MemorySegment {

    /**
     * Put value in memory
     *
     * @return pointer to value in the current segment
     */
    int put(Object value);

    /**
     * Remove value from memory and free pointer for reuse. Note that it won't destroy object because it is not possible.
     *
     * @param pointer pointer to value returned by {@link #put(Object)}
     * @return value
     */
    <T> T destroy(int pointer);

    /**
     * Get value from memory by pointer
     *
     * @param pointer pointer to value returned by {@link #put(Object)}
     * @return value
     */
    <T> T get(int pointer);

    /**
     * @return name of the type associated with this memory segment
     */
    String getType();

    /**
     * @return id of the type used to get memory segment from memory
     */
    int getTypeID();

}
