package org.panda_lang.panda;

import org.panda_lang.core.work.Wrapper;

public class PandaScript {

    private Wrapper wrapper;
    private String workingDirectory;

    public PandaScript(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

}
