package org.panda_lang.panda.lang.memory;

import org.panda_lang.core.memory.Memory;
import org.panda_lang.core.memory.MemorySegment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PandaMemory implements Memory {

    private final AtomicInteger typeIDAssigner;
    private final List<MemorySegment> memorySegments;

    public PandaMemory() {
        this.typeIDAssigner = new AtomicInteger();
        this.memorySegments = new ArrayList<>();
    }

    @Override
    public int allocate(String type) {
        int id = typeIDAssigner.getAndIncrement();
        PandaMemorySegment pandaMemorySegment = new PandaMemorySegment(id, type);
        memorySegments.add(id, pandaMemorySegment);
        return id;
    }

    @Override
    public int allocate(MemorySegment memorySegment) {
        int id = typeIDAssigner.getAndIncrement();
        memorySegments.add(id, memorySegment);
        return id;
    }

    @Override
    public MemorySegment get(int typeID) {
        return memorySegments.get(typeID);
    }

}
