package org.panda_lang.core.work;

public class Value {

    private Object value;

    public Value() {
    }

    public Value(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
