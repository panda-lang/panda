package org.panda_lang.core.work;

import java.util.List;

public interface Wrapper extends Executable {

    ExecutableCell addExecutable(Executable executable);

    List<ExecutableCell> getExecutableCells();

}
