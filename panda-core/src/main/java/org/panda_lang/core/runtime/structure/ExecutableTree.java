package org.panda_lang.core.runtime.structure;

import org.panda_lang.core.runtime.Value;
import org.panda_lang.core.runtime.element.Executable;

public interface ExecutableTree {

    /**
     * Creates executable-tree based on specified executable
     *
     * @param executable root of the tree
     * @param value parameters
     * @return result of executed code
     */
    Value call(Executable executable, Value... value);



}
