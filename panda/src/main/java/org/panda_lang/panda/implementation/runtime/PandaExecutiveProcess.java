package org.panda_lang.panda.implementation.runtime;

import org.panda_lang.core.memory.Memory;
import org.panda_lang.core.runtime.ExecutiveProcess;
import org.panda_lang.core.runtime.element.ExecutableTree;
import org.panda_lang.core.structure.Application;
import org.panda_lang.core.structure.Executable;
import org.panda_lang.core.structure.Value;
import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.panda.implementation.runtime.element.PandaExecutableTree;

public class PandaExecutiveProcess implements ExecutiveProcess, Executable {

    private final Application application;
    private final Wrapper wrapper;
    private final ExecutableTree executableTree;
    private final Value[] values;

    public PandaExecutiveProcess(Application application, Wrapper wrapper, Value... values) {
        this.application = application;
        this.wrapper = wrapper;
        this.executableTree = new PandaExecutableTree();
        this.values = values;
    }

    @Override
    public Value execute(Value... parameters) {
        return executableTree.call(wrapper, parameters);
    }

    @Override
    public void run() {
        executableTree.call(wrapper, values);
    }

    @Override
    public ExecutableTree getExecutableTree() {
        return null;
    }

    @Override
    public Memory getMemory() {
        return application.getMemory();
    }

}
