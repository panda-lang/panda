package org.panda_lang.panda.composition.element;

import org.panda_lang.core.work.Executable;
import org.panda_lang.core.work.ExecutableCell;
import org.panda_lang.core.work.NamedWrapper;
import org.panda_lang.core.work.Value;
import org.panda_lang.panda.implementation.work.PandaExecutableCell;

import java.util.List;

public class Block implements NamedWrapper {

    protected String name;
    protected List<ExecutableCell> executableCells;

    @Override
    public Value execute() {
        for (ExecutableCell executableCell : executableCells) {
            Executable executable = executableCell.getExecutable();

            if (executable == null) {
                continue;
            }

            executable.execute();
        }
        return null;
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

    @Override
    public String getName() {
        return name;
    }

}
