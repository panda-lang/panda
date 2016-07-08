package org.panda_lang.core.memory;

import java.util.concurrent.atomic.AtomicInteger;

public interface Memory {

    int allocate(String type);

    int allocate(MemorySegment memorySegment);

    MemorySegment get(int typeID);

}
