package org.panda_lang.core.structure;

/**
 * ExecutableCell is a mutable container for {@link Executable}
 */
public interface ExecutableCell {

    void setExecutable(Executable executable);

    Executable getExecutable();

}
