package org.panda_lang.panda.implementation.work.element;

import org.panda_lang.core.work.Value;
import org.panda_lang.core.work.element.ExecutablePrototype;
import org.panda_lang.core.work.element.Wrapper;
import org.panda_lang.core.work.element.WrapperInstance;
import org.panda_lang.core.work.structure.ExecutableCell;
import org.panda_lang.panda.implementation.work.structure.PandaExecutableCell;

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
    public ExecutableCell addExecutable(ExecutablePrototype executablePrototype) {
        ExecutableCell executableCell = new PandaExecutableCell(executablePrototype);
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

    public static class PandaWrapperInstance implements WrapperInstance {

        private final PandaWrapper pandaWrapper;

        public PandaWrapperInstance(PandaWrapper pandaWrapper) {
            this.pandaWrapper = pandaWrapper;
        }

        @Override
        public Value execute(Value... values) {
            return null;
        }

        @Override
        public Wrapper getWrapper() {
            return null;
        }

    }

}
