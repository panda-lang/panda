package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

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
    public Essence run(Particle particle) {
        if (executable != null) {
            return executable.run(particle);
        }
        return null;
    }

    @Override
    public String getName() {
        return methodName;
    }

}
