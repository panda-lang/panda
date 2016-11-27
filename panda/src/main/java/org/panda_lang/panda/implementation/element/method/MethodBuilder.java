package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.framework.structure.Executable;

public class MethodBuilder {

    private String methodName;
    private Executable methodBody;
    private boolean isStatic;
    private MethodVisibility visibility;


    public MethodBuilder methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public MethodBuilder methodBody(Executable executable) {
        this.methodBody = executable;
        return this;
    }

    public MethodBuilder isStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public MethodBuilder visibility(MethodVisibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public Method build() {
        return new Method(methodName, methodBody, isStatic, visibility);
    }

}
