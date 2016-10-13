package org.panda_lang.core.work.element;

import java.util.List;

public interface Scope {

    ExecutableCell addExecutable(Executable executable);

    List<ExecutableCell> getExecutableCells();

    String getName();

}
