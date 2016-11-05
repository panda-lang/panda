package org.panda_lang.core.runtime.element;

import org.panda_lang.core.runtime.structure.ExecutableCell;

import java.util.List;

public interface Scope extends Executable {

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

    /**
     * @return name of the current scope, format depends on the scope
     */
    String getName();

}
