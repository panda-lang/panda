package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.Executable;

public class Method implements Executable {

    private final String methodName;
    private final Executable methodBody;
    private final boolean isStatic;
    private MethodVisibility visibility;

    public Method(String methodName, Executable methodBody, boolean isStatic, MethodVisibility visibility) {
        this.methodName = methodName;
        this.methodBody = methodBody;
        this.isStatic = isStatic;
        this.visibility = visibility;
    }

    @Override
    public void execute(ExecutableBridge executionInfo) {

    }

    public boolean isStatic() {
        return isStatic;
    }

    public MethodVisibility getVisibility() {
        return visibility;
    }

    public Executable getMethodBody() {
        return methodBody;
    }

    public String getMethodName() {
        return methodName;
    }

    public static MethodBuilder builder() {
        return new MethodBuilder();
    }

}
