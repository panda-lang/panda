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

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.utilities.commons.math.BitwiseUtils;

public interface Memory {

    /**
     * @param type name of segment type
     * @return type id of the created segment
     */
    int allocate(String type);

    /**
     * Allows you to allocate custom memory segment.
     * If you are not sure we strongly recommend use {@link #allocate(String)}
     *
     * @param memorySegment specified custom segment
     * @return segment id of the specified segment
     */
    int allocate(MemorySegment memorySegment);

    /**
     * @param typeID id of the type returned by {@link #allocate(String)}
     * @return memory segment associated with specified type id
     */
    MemorySegment get(int typeID);

    /**
     * @param pointer type id and index of value as long
     * @return value
     */
    default @Nullable Object get(long pointer) {
        MemorySegment segment = get(BitwiseUtils.extractLeft(pointer));

        if (segment == null) {
            return null;
        }

        return segment.get(BitwiseUtils.extractRight(pointer));
    }

}
