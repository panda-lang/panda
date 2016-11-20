package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.structure.Executable;
import org.panda_lang.panda.implementation.element.struct.ClassPrototype;

public class MethodBuilder {

    private ClassPrototype classPrototype;
    private String methodName;
    private Executable methodBody;
    private boolean isStatic;
    private MethodVisibility visibility;

    public MethodBuilder classPrototype(ClassPrototype classPrototype) {
        this.classPrototype = classPrototype;
        return this;
    }

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
        return new Method(classPrototype, methodName, methodBody, isStatic, visibility);
    }

}
