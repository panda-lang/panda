package org.panda_lang.panda.implementation.runtime.structure;

import org.panda_lang.core.runtime.element.Value;
import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.core.runtime.structure.ExecutableBranch;
import org.panda_lang.core.runtime.structure.ExecutableTree;

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
