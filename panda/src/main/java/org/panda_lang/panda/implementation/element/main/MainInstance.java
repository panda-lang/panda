package org.panda_lang.panda.implementation.element.main;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.Wrapper;
import org.panda_lang.framework.structure.WrapperInstance;

public class MainInstance implements WrapperInstance {

    @Override
    public void execute(ExecutableBridge executiveProcess) {

    }

    @Override
    public Object[] getVariables() {
        return new Object[0];
    }

    @Override
    public Wrapper getWrapper() {
        return null;
    }

}
