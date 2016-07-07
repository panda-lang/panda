package org.panda_lang.panda.lang.work;

import org.panda_lang.core.work.Value;

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
