package org.panda_lang.core.work.element;

import org.panda_lang.core.work.structure.ExecutableCell;

import java.util.List;

public interface Scope {

    ExecutableCell addExecutable(ExecutablePrototype executable);

    List<ExecutableCell> getExecutableCells();

    String getName();

}
