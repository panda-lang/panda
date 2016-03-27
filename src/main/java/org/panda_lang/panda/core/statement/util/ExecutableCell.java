package org.panda_lang.panda.core.statement.util;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.Executable;

public class ExecutableCell implements Executable {

    private final int id;
    private Executable executable;

    public ExecutableCell(int id, Executable executable) {
        this(id);
        this.executable = executable;
    }

    public ExecutableCell(int id) {
        this.id = id;
    }

    @Override
    public Essence run(Alice alice) {
        return executable.run(alice);
    }

    public Executable getExecutable() {
        return executable;
    }

    public void setExecutable(NamedExecutable executable) {
        this.executable = executable;
    }

    public int getID() {
        return id;
    }

}
