package org.panda_lang.panda.implementation.runtime.element;

import org.panda_lang.core.runtime.element.Executable;
import org.panda_lang.core.runtime.element.Scope;
import org.panda_lang.core.runtime.structure.ExecutableCell;
import org.panda_lang.panda.implementation.runtime.structure.PandaExecutableCell;

import java.util.ArrayList;
import java.util.List;

public abstract class PandaScope implements Scope {

    private final List<ExecutableCell> executableCells;

    public PandaScope() {
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
