package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Alice;

public class Method implements NamedExecutable {

    private final String methodName;
    private final Executable executable;

    public Method(NamedExecutable executable) {
        this(executable.getName(), executable);
    }

    public Method(String methodName, Executable executable) {
        this.methodName = methodName;
        this.executable = executable;
    }

    @Override
    public Essence run(Alice alice) {
        if (executable != null) {
            return executable.run(alice);
        }
        return null;
    }

    @Override
    public String getName() {
        return methodName;
    }

}
