package org.panda_lang.core.runtime.structure;

import org.panda_lang.core.runtime.element.Executable;

/**
 * ExecutableCell is a mutable container for {@link Executable}
 */
public interface ExecutableCell {

    void setExecutable(Executable executable);

    Executable getExecutable();

}
