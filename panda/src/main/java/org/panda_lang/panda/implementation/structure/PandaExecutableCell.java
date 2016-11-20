package org.panda_lang.panda.implementation.structure;

import org.panda_lang.core.structure.Executable;
import org.panda_lang.core.structure.ExecutableCell;

public class PandaExecutableCell implements ExecutableCell {

    private Executable executable;
    private boolean manipulated;

    public PandaExecutableCell(Executable executable) {
        this.executable = executable;
    }

    public void setExecutable(Executable executable) {
        if (this.executable != null) {
            this.manipulated = true;
        }

        this.executable = executable;
    }

    public boolean isManipulated() {
        return manipulated;
    }

    @Override
    public Executable getExecutable() {
        return executable;
    }

}
