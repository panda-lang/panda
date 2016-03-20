package org.panda_lang.panda.core.memory.successor;

import org.panda_lang.panda.core.syntax.Field;

public class MemoryCompartment {

    private final MemoryCell[] compartment;

    public MemoryCompartment() {
        this.compartment = new MemoryCell[Field.getIdentifier().get()];
    }

    public void put(int id, Object value) {
        MemoryCell memoryCell = compartment[id];
        if (memoryCell == null) {
            memoryCell = new MemoryCell(id);
            compartment[id] = memoryCell;
        }
        memoryCell.setValue(value);
    }

    public MemoryCell get(int id) {
        return compartment[id];
    }

    public MemoryCell[] getCompartment() {
        return compartment;
    }

}
