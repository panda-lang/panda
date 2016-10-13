package org.panda_lang.panda.implementation.work;

import org.panda_lang.core.work.element.Executable;
import org.panda_lang.core.work.element.ExecutableCell;
import org.panda_lang.core.work.element.Wrapper;
import org.panda_lang.core.work.element.WrapperInstance;

import java.util.ArrayList;
import java.util.List;

public class PandaWrapper implements Wrapper {

    private final List<ExecutableCell> executableCells;

    public PandaWrapper() {
        this.executableCells = new ArrayList<>();
    }

    @Override
    public WrapperInstance createInstance() {
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
        return null;
    }

    public static class PandaWrapperInstance {



    }

}
