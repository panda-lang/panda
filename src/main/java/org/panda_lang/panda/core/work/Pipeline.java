package org.panda_lang.panda.core.work;

import java.util.ArrayList;
import java.util.List;

public class Pipeline implements Executable {

    private final List<Executable> executables;

    public Pipeline() {
        this.executables = new ArrayList<>();
    }

    @Override
    public void execute() {
        for (Executable executable : executables) {
            executable.execute();
        }
    }

    public void addLast(Executable executable) {
        executables.add(executable);
    }

    public List<Executable> getExecutables() {
        return executables;
    }

}
