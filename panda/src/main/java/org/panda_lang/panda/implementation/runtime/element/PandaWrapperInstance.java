package org.panda_lang.panda.implementation.runtime.element;

import org.panda_lang.core.runtime.element.Value;
import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;

public class PandaWrapperInstance implements WrapperInstance {

    private final Wrapper wrapper;

    public PandaWrapperInstance(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Value execute(Value... parameters) {
        return null;
    }

    @Override
    public int[] getPointers() {
        return new int[0];
    }

    @Override
    public Wrapper getWrapper() {
        return wrapper;
    }

}
