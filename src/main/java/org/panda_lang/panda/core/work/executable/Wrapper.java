package org.panda_lang.panda.core.work.executable;

import org.panda_lang.panda.core.work.executable.Executable;

import java.util.ArrayList;
import java.util.List;

public class Wrapper implements Executable {

    private final List<Executable> executableList = new ArrayList<>();

    @Override
    public void execute() {
        for (Executable executable : executableList) {
            executable.execute();
        }
    }

    public void addExecutable(Executable executable) {
        executableList.add(executable);
    }

    public List<Executable> getExecutableList() {
        return executableList;
    }

}
