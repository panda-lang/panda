package org.panda_lang.panda;

import org.panda_lang.core.runtime.element.Executable;
import org.panda_lang.core.runtime.element.Value;
import org.panda_lang.core.runtime.structure.ExecutableCell;
import org.panda_lang.panda.implementation.runtime.element.PandaScope;
import org.panda_lang.panda.implementation.runtime.element.PandaWrapper;

import java.util.List;

public class PandaScript extends PandaScope implements Executable {

    private final String scriptName;
    private final PandaWrapper pandaWrapper;

    public PandaScript(String scriptName, PandaWrapper pandaWrapper) {
        this.scriptName = scriptName;
        this.pandaWrapper = pandaWrapper;
    }

    @Override
    public Value execute(Value... parameters) {
        List<ExecutableCell> executableCells = pandaWrapper.getExecutableCells();
        return null;
    }

    public PandaWrapper getPandaWrapper() {
        return pandaWrapper;
    }

    public String getScriptName() {
        return scriptName;
    }

}
