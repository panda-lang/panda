package org.panda_lang.panda.implementation.work;

import org.panda_lang.core.work.Executable;
import org.panda_lang.core.work.ExecutableCell;
import org.panda_lang.core.work.Value;
import org.panda_lang.core.work.Wrapper;

import java.util.ArrayList;
import java.util.List;

public class PandaWrapper implements Wrapper {

    private final List<ExecutableCell> executableCells;

    public PandaWrapper() {
        this.executableCells = new ArrayList<>();
    }

    @Override
    public Value execute() {
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

}
