package org.panda_lang.panda.implementation.memory;

import org.panda_lang.core.memory.MemorySegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public class PandaMemorySegment implements MemorySegment {

    private final int typeID;
    private final String type;
    private final AtomicInteger instanceIDAssigner;
    private final Stack<Integer> free;
    private final List<Object> instances;

    public PandaMemorySegment(int typeID, String type) {
        this.typeID = typeID;
        this.type = type;
        this.instanceIDAssigner = new AtomicInteger();
        this.free = new Stack<>();
        this.instances = new ArrayList<>();
    }

    @Override
    public int put(Object value) {
        if (free.size() > 0) {
            int id = free.pop();
            instances.set(id, value);
            return id;
        }
        else {
            int id = instanceIDAssigner.getAndIncrement();
            fillAndSet(id, value);
            return id;
        }
    }

    protected void fillAndSet(int index, Object value) {
        if (index > (instances.size() - 1)) {
            for (int i = instances.size(); i < index; i++) {
                instances.add(null);
            }
            instances.add(value);
        }
        else {
            instances.set(index, value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(int pointer) {
        return instances.size() > pointer ? (T) instances.get(pointer) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T destroy(int pointer) {
        if (instances.size() > pointer) {
            Object element = instances.remove(pointer);
            free.push(pointer);
            return (T) element;
        }
        return null;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getTypeID() {
        return typeID;
    }

}
