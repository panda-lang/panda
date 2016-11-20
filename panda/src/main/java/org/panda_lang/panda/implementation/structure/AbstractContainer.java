package org.panda_lang.panda.implementation.structure;

import org.panda_lang.core.structure.Container;
import org.panda_lang.core.structure.Executable;
import org.panda_lang.core.structure.ExecutableCell;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainer implements Container {

    protected final List<ExecutableCell> executableCells;

    public AbstractContainer() {
        this.executableCells = new ArrayList<>();
    }

    @Override
    public ExecutableCell addExecutable(Executable executable) {
        ExecutableCell executableCell = new PandaExecutableCell(executable);
        executableCells.add(executableCell);
        return executableCell;
    }

    @Override
    public List<ExecutableCell> getExecutableCells() {
        return executableCells;
    }

}
