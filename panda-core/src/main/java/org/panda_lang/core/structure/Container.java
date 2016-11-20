package org.panda_lang.core.structure;

import java.util.List;

public interface Container extends Statement {

    /**
     * Adds executable to the current scope
     *
     * @param executable proper executable
     * @return executable cell where executable was placed
     */
    ExecutableCell addExecutable(Executable executable);

    /**
     * @return list of all cells in correct order
     */
    List<ExecutableCell> getExecutableCells();

}
