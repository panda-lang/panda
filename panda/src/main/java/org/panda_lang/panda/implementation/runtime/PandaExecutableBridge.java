package org.panda_lang.panda.implementation.runtime;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.Application;
import org.panda_lang.framework.structure.Executable;
import org.panda_lang.framework.structure.Value;
import org.panda_lang.framework.structure.Wrapper;

public class PandaExecutableBridge implements ExecutableBridge {

    private final Application application;
    private final Wrapper wrapper;
    private final Value[] parametersValues;

    public PandaExecutableBridge(Application application, Wrapper wrapper, Value... parametersValues) {
        this.application = application;
        this.wrapper = wrapper;
        this.parametersValues = parametersValues;
    }

    @Override
    public void call(Executable executable) {

    }

    @Override
    public void returnValue(Value value) {

    }

    @Override
    public Value[] getParameters() {
        return parametersValues;
    }

}
