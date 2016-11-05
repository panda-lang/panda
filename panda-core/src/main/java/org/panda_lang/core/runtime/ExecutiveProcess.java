package org.panda_lang.core.runtime;

import org.panda_lang.core.memory.Memory;
import org.panda_lang.core.runtime.structure.ExecutableTree;

public interface ExecutiveProcess {

    ExecutableTree getExecutableTree();

    Memory getMemory();

}
