package org.panda_lang.core.work;

import org.panda_lang.core.memory.Memory;
import org.panda_lang.core.work.structure.ExecutableTree;

public interface ExecutiveProcess {

    ExecutableTree getExecutableTree();

    Memory getMemory();

}
