package org.panda_lang.panda.composition.element;

import org.panda_lang.core.work.element.Executable;
import org.panda_lang.core.work.element.ExecutableCell;
import org.panda_lang.core.work.element.Scope;
import org.panda_lang.panda.implementation.work.PandaExecutableCell;

import java.util.List;

public class PandaScope implements Scope {

    protected String name;
    protected List<ExecutableCell> executableCells;

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

    @Override
    public String getName() {
        return name;
    }

}
