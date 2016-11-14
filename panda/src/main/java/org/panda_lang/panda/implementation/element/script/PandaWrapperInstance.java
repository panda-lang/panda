package org.panda_lang.panda.implementation.element.script;

import org.panda_lang.core.runtime.element.Executable;
import org.panda_lang.core.runtime.element.Value;
import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.core.runtime.structure.ExecutableBranch;
import org.panda_lang.core.runtime.structure.ExecutableCell;

public class PandaWrapperInstance implements WrapperInstance {

    private final PandaWrapper wrapper;
    private final Object[] variables;

    public PandaWrapperInstance(PandaWrapper pandaWrapper) {
        this.wrapper = pandaWrapper;
        this.variables = new Object[pandaWrapper.getFields().size()];
    }

    @Override
    public Value execute(ExecutableBranch executableBranch, Value... values) {
        for (ExecutableCell cell : wrapper.getExecutableCells()) {
            Executable executable = cell.getExecutable();

            if (executable instanceof Wrapper) {
                continue;
            }

            executable.execute(values);
        }

        return null;
    }

    @Override
    public Object[] getVariables() {
        return variables;
    }

    @Override
    public Wrapper getWrapper() {
        return wrapper;
    }

}