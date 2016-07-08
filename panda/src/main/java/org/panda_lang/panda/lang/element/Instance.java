package org.panda_lang.panda.lang.element;

import java.util.ArrayList;

public class Instance {

    private final ClassPrototype classPrototype;
    private final int[] fields;

    public Instance(ClassPrototype classPrototype) {
        this.classPrototype = classPrototype;
        this.fields = new int[classPrototype.getFields().length];
    }

    public int getFieldValue(int i) {
        return fields[i];
    }

    public int[] getFields() {
        return fields;
    }

    public ClassPrototype getClassPrototype() {
        return classPrototype;
    }

}
