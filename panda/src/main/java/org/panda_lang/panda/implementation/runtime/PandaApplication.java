package org.panda_lang.panda.implementation.runtime;

import org.panda_lang.core.runtime.Application;
import org.panda_lang.core.memory.Memory;
import org.panda_lang.panda.implementation.element.script.PandaScript;
import org.panda_lang.panda.implementation.memory.PandaMemory;

import java.util.ArrayList;
import java.util.Collection;

public class PandaApplication implements Application {

    private final Memory memory;
    private final Collection<PandaScript> pandaScripts;
    private String workingDirectory;

    public PandaApplication() {
        this.memory = new PandaMemory();
        this.pandaScripts = new ArrayList<>();
    }

    @Override
    public void launch(String[] arguments) {

    }

    public void addPandaScript(PandaScript pandaScript) {
        pandaScripts.add(pandaScript);
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Collection<PandaScript> getPandaScripts() {
        return pandaScripts;
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
