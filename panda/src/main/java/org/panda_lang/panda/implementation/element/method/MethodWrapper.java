package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.structure.WrapperInstance;
import org.panda_lang.panda.implementation.element.parameter.Parameter;
import org.panda_lang.panda.implementation.structure.AbstractContainer;

import java.util.List;

public class MethodWrapper extends AbstractContainer implements Wrapper {

    private final int wrapperID;
    private final String methodName;
    private final List<Parameter> parameters;

    public MethodWrapper(int wrapperID, String methodName, List<Parameter> parameters) {
        this.wrapperID = wrapperID;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    @Override
    public WrapperInstance createInstance() {
        return null;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public int getID() {
        return 0;
    }

}
