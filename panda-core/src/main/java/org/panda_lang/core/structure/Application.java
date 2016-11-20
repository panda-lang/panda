package org.panda_lang.core.structure;

import org.panda_lang.core.memory.Memory;

import java.util.List;

public interface Application {

    /**
     * Launch application with the specified arguments
     */
    void launch(String[] arguments);

    /**
     * @return a list of belonging to the application scripts
     */
    List<Script> getScripts();

    /**
     * @return directory where application was called
     */
    String getWorkingDirectory();

    /**
     * @return a memory used by application
     */
    Memory getMemory();

}
