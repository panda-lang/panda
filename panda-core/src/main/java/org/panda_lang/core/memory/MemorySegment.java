package org.panda_lang.core.memory;

public interface MemorySegment {

    int put(Object value);

    <T> T get(int pointer);

    String getType();

    int getTypeID();

}
