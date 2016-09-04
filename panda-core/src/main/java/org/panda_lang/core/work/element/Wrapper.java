package org.panda_lang.core.work.element;

import org.panda_lang.core.work.ExecutableCell;

import java.util.List;

public interface Wrapper extends Executable {

    ExecutableCell addExecutable(Executable executable);

    List<ExecutableCell> getExecutableCells();

}
