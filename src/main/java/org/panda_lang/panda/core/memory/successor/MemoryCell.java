package org.panda_lang.panda.core.memory.successor;

public class MemoryCell {

    private final int id;
    private Object value;

    public MemoryCell(int id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getID() {
        return id;
    }

}
