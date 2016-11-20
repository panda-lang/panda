package org.panda_lang.panda.implementation.structure;

import org.panda_lang.core.memory.Memory;
import org.panda_lang.core.structure.Application;
import org.panda_lang.core.structure.Script;
import org.panda_lang.panda.implementation.memory.PandaMemory;

import java.util.ArrayList;
import java.util.List;

public class PandaApplication implements Application {

    private final Memory memory;
    private final List<Script> scripts;
    private String workingDirectory;

    public PandaApplication() {
        this.memory = new PandaMemory();
        this.scripts = new ArrayList<>();
    }

    @Override
    public void launch(String[] arguments) {

    }

    public void addPandaScript(Script pandaScript) {
        scripts.add(pandaScript);
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public List<Script> getScripts() {
        return scripts;
    }

    @Override
    public String getWorkingDirectory() {
        return workingDirectory;
    }

    @Override
    public Memory getMemory() {
        return memory;
    }

}
