package org.panda_lang.framework.memory;

import org.panda_lang.framework.util.BitwiseUtils;

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
    default Object get(long pointer) {
        MemorySegment segment = get(BitwiseUtils.extractLeft(pointer));

        if (segment == null) {
            return null;
        }

        return segment.get(BitwiseUtils.extractRight(pointer));
    }

}
