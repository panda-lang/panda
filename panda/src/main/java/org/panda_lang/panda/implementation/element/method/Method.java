package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.core.runtime.element.Executable;
import org.panda_lang.core.runtime.element.Value;
import org.panda_lang.panda.implementation.element.struct.ClassPrototype;

public class Method implements Executable {

    private final ClassPrototype classPrototype;
    private final String methodName;
    private final Executable methodBody;
    private final boolean isStatic;

    public Method(ClassPrototype classPrototype, String methodName, Executable methodBody, boolean isStatic) {
        this.classPrototype = classPrototype;
        this.methodName = methodName;
        this.methodBody = methodBody;
        this.isStatic = isStatic;
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

    public ClassPrototype getClassPrototype() {
        return classPrototype;
    }

    public Executable getMethodBody() {
        return methodBody;
    }

    public String getMethodName() {
        return methodName;
    }

}
