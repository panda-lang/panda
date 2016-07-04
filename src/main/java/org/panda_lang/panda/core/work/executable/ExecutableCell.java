package org.panda_lang.panda.core.work.executable;

public class ExecutableCell {

    private Executable executable;

    public ExecutableCell() {
    }

    public ExecutableCell(Executable executable) {
        this.executable = executable;
    }

    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    public Executable getExecutable() {
        return executable;
    }

}
