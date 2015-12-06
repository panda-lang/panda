package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.NamedExecutable;

public class MethodScheme {

    private final String name;
    private final Executable executable;

    public MethodScheme(NamedExecutable executable) {
        this(executable.getName(), executable);
    }

    public MethodScheme(String name, Executable executable) {
        this.name = name;
        this.executable = executable;
    }

    public Executable getExecutable() {
        return executable;
    }

    public String getName() {
        return name;
    }

}
