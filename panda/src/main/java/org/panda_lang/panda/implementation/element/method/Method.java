package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.structure.Executable;
import org.panda_lang.core.structure.Value;
import org.panda_lang.panda.implementation.element.struct.ClassPrototype;

public class Method implements Executable {

    private final ClassPrototype classPrototype;
    private final String methodName;
    private final Executable methodBody;
    private final boolean isStatic;
    private MethodVisibility visibility;

    public Method(ClassPrototype classPrototype, String methodName, Executable methodBody, boolean isStatic, MethodVisibility visibility) {
        this.classPrototype = classPrototype;
        this.methodName = methodName;
        this.methodBody = methodBody;
        this.isStatic = isStatic;
        this.visibility = visibility;
    }

    @Override
    public Value execute(Value... parametersValues) {
        return methodBody.execute(parametersValues);
    }

    public boolean isClassMember() {
        return classPrototype != null;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public MethodVisibility getVisibility() {
        return visibility;
    }

    public ClassPrototype getClassPrototype() {
        return classPrototype;
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
