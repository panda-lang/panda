package org.panda_lang.panda.implementation.runtime.element;

import org.panda_lang.core.structure.Value;
import org.panda_lang.core.structure.Wrapper;
import org.panda_lang.core.structure.WrapperInstance;
import org.panda_lang.core.runtime.element.ExecutableBranch;
import org.panda_lang.core.runtime.element.ExecutableTree;

public class PandaExecutableTree implements ExecutableTree {

    private ExecutableBranch baseBranch;

    @Override
    public Value call(Wrapper wrapper, Value... parameters) {
        WrapperInstance wrapperInstance = wrapper.createInstance();
        this.baseBranch = new PandaExecutableBranch(wrapperInstance);

        return wrapperInstance.execute(baseBranch, parameters);
    }

    @Override
    public ExecutableBranch getBaseBranch() {
        return baseBranch;
    }

}
