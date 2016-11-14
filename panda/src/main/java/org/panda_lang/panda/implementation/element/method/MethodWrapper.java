package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.panda.implementation.runtime.element.PandaScope;

public class MethodWrapper extends PandaScope implements Wrapper {

    private final int wrapperID;
    private final String methodName;

    public MethodWrapper(int wrapperID, String methodName) {
        this.wrapperID = wrapperID;
        this.methodName = methodName;
    }

    @Override
    public WrapperInstance createInstance() {
        return new MethodWrapperInstance(this);
    }

    @Override
    public int getID() {
        return wrapperID;
    }

}
