package org.panda_lang.core;

import org.panda_lang.core.memory.Memory;

public interface Application {

    void launch(String[] arguments);

    /**
     * @return directory where application was called
     */
    String getWorkingDirectory();

    /**
     * @return a memory used by application
     */
    Memory getMemory();

}
