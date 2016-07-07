package org.panda_lang.panda.lang.work;

import org.panda_lang.core.work.Executable;
import org.panda_lang.core.work.ExecutableCell;

public class PandaExecutableCell implements ExecutableCell {

    private Executable executable;

    public PandaExecutableCell() {
    }

    public PandaExecutableCell(Executable executable) {
        this.executable = executable;
    }

    @Override
    public void setExecutable(Executable executable) {
        this.executable = executable;
    }

    @Override
    public Executable getExecutable() {
        return executable;
    }

}
