package org.panda_lang.core.memory;

public interface MemorySegment {

    /**
     * Put value in memory
     *
     * @return pointer to value in the current segment
     */
    int put(Object value);

    /**
     * Get value from memory by pointer
     *
     * @param pointer pointer to value returned by {@link #put(Object)}
     * @return value
     */
    <T> T get(int pointer);

    /**
     * Remove value from memory and free pointer for reuse. Note that it won't destroy object because it is not possible.
     *
     * @param pointer pointer to value returned by {@link #put(Object)}
     * @return value
     */
    <T> T destroy(int pointer);

    /**
     * @return name of the type associated with this memory segment
     */
    String getType();

    /**
     * @return id of the type used to get memory segment from memory
     */
    int getTypeID();

}
