package org.panda_lang.core.runtime.element;

import org.panda_lang.core.structure.Value;
import org.panda_lang.core.structure.Wrapper;

public interface ExecutableTree {

    /**
     * Creates executable-tree based on specified executable
     *
     * @param wrapper root of the tree
     * @param value   parameters
     * @return result of executed code
     */
    Value call(Wrapper wrapper, Value... value);

    /**
     * @return branch associated with specified in call wrapper
     */
    ExecutableBranch getBaseBranch();

}
