package org.panda_lang.core.runtime;

import org.panda_lang.core.memory.Memory;
import org.panda_lang.core.runtime.element.ExecutableTree;

public interface ExecutiveProcess extends Runnable {

    /**
     * @return executable tree of the current process
     */
    ExecutableTree getExecutableTree();

    /**
     * @return a memory used by this process
     */
    Memory getMemory();

}
