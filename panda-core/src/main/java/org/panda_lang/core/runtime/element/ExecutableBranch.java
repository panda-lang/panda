package org.panda_lang.core.runtime.element;

import org.panda_lang.core.structure.WrapperInstance;

public interface ExecutableBranch {

    /**
     * @return recently called branch by this branch
     */
    ExecutableBranch getChild();

    /**
     * @return executable branch which called current branch
     */
    ExecutableBranch getParent();

    /**
     * @return associated instance of wrapper
     */
    WrapperInstance getWrapperInstance();

}
