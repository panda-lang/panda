package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.structure.WrapperInstance;
import org.panda_lang.panda.implementation.structure.AbstractContainer;

public class MethodWrapper extends AbstractContainer implements Wrapper {

    private final int wrapperID;
    private final Method method;

    public MethodWrapper(int wrapperID, Method method) {
        this.wrapperID = wrapperID;
        this.method = method;
    }

    @Override
    public WrapperInstance createInstance() {
        return new MethodWrapperInstance(this);
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String getName() {
        return method.getMethodName();
    }

    @Override
    public int getID() {
        return wrapperID;
    }

}
