package org.panda_lang.core;

import org.panda_lang.core.memory.Memory;

public interface Application {

    void launch(String[] arguments);

    Memory getMemory();

}
