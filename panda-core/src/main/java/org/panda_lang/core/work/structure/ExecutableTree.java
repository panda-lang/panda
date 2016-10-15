package org.panda_lang.core.work.structure;

import org.panda_lang.core.work.Value;
import org.panda_lang.core.work.element.Executable;

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
