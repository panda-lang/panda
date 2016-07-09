package org.panda_lang.panda.lang.element;

public class Instance {

    private final ClassPrototype classPrototype;
    private final int[] fields;

    public Instance(ClassPrototype classPrototype) {
        this.classPrototype = classPrototype;
        this.fields = new int[classPrototype.getFields().length];
    }

    public <T> T getFieldValue(int i) {
        return null;
    }

    public int[] getFields() {
        return fields;
    }

    public ClassPrototype getClassPrototype() {
        return classPrototype;
    }

}
