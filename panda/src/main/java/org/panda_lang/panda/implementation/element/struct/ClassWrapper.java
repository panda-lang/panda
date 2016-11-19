package org.panda_lang.panda.implementation.element.struct;

import org.panda_lang.core.runtime.element.Executable;
import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.core.runtime.structure.ExecutableCell;

import java.util.List;

public class ClassWrapper implements Wrapper {

    @Override
    public WrapperInstance createInstance() {
        return null;
    }

    @Override
    public ExecutableCell addExecutable(Executable executable) {
        return null;
    }

    @Override
    public List<ExecutableCell> getExecutableCells() {
        return null;
    }

    @Override
    public int getID() {
        return 0;
    }

}
