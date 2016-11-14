package org.panda_lang.panda.implementation.element.script;

import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.panda.implementation.element.field.Field;
import org.panda_lang.panda.implementation.runtime.element.PandaScope;

import java.util.ArrayList;
import java.util.List;

public class PandaWrapper extends PandaScope implements Wrapper {

    private static final int PANDA_WRAPPER_ID = 0;

    private final List<Field> fields;

    public PandaWrapper() {
        this.fields = new ArrayList<>();
    }

    @Override
    public WrapperInstance createInstance() {
        return new PandaWrapperInstance(this);
    }

    public List<Field> getFields() {
        return fields;
    }

    @Override
    public int getID() {
        return PANDA_WRAPPER_ID;
    }

}
