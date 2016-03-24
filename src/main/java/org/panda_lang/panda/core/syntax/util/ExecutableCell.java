package org.panda_lang.panda.core.syntax.util;

import org.panda_lang.panda.core.syntax.NamedExecutable;

public class ExecutableCell {

    private final int id;
    private NamedExecutable executable;

    public ExecutableCell(int id, NamedExecutable executable) {
        this(id);
        this.executable = executable;
    }

    public ExecutableCell(int id) {
        this.id = id;
    }

    public NamedExecutable getExecutable() {
        return executable;
    }

    public void setExecutable(NamedExecutable executable) {
        this.executable = executable;
    }

    public int getID() {
        return id;
    }

}
