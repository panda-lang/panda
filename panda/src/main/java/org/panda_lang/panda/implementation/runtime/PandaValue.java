package org.panda_lang.panda.implementation.runtime;

import org.panda_lang.core.runtime.element.Value;

public class PandaValue implements Value {

    private Object value;

    public PandaValue() {
    }

    public PandaValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

}
