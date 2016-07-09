package org.panda_lang.core.memory;

public interface Memory {

    int allocate(String type);

    int allocate(MemorySegment memorySegment);

    MemorySegment get(int typeID);

}
