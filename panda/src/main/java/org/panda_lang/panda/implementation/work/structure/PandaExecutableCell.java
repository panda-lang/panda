package org.panda_lang.panda.implementation.work.structure;

import org.panda_lang.core.work.element.ExecutablePrototype;
import org.panda_lang.core.work.structure.ExecutableCell;

public class PandaExecutableCell implements ExecutableCell {

    private ExecutablePrototype executablePrototype;
    private boolean manipulated;

    public PandaExecutableCell(ExecutablePrototype executablePrototype) {
        this.executablePrototype = executablePrototype;
    }

    @Override
    public void setExecutablePrototype(ExecutablePrototype executablePrototype) {
        if (this.executablePrototype != null) {
            this.manipulated = true;
        }

        this.executablePrototype = executablePrototype;
    }

    public boolean isManipulated() {
        return manipulated;
    }

    @Override
    public ExecutablePrototype getExecutablePrototype() {
        return executablePrototype;
    }

}
