package org.panda_lang.panda.core.scheme;

import org.panda_lang.panda.core.syntax.IExecutable;

public class MethodScheme {

    private final String name;
    private final IExecutable executable;

    public MethodScheme(String name, IExecutable executable){
        this.name = name;
        this.executable = executable;
    }

    public IExecutable getExecutable() {
        return executable;
    }

    public String getName() {
        return name;
    }

}
