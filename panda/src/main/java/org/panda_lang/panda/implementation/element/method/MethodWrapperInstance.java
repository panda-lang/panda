package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.structure.Value;
import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.structure.WrapperInstance;
import org.panda_lang.core.runtime.element.ExecutableBranch;

public class MethodWrapperInstance implements WrapperInstance {

    private final MethodWrapper wrapper;

    public MethodWrapperInstance(MethodWrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Value execute(ExecutableBranch executableBranch, Value... parameters) {
        return null;
    }

    @Override
    public Object[] getVariables() {
        return new Object[0];
    }

    @Override
    public Wrapper getWrapper() {
        return wrapper;
    }

}
