package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Structure;

public class MethodInfo {

    private final String methodName;
    private final RuntimeValue[] runtimeValues;
    private RuntimeValue instance;
    private Structure structure;

    public MethodInfo(String methodName, RuntimeValue... runtimeValues) {
        this.methodName = methodName;
        this.runtimeValues = runtimeValues;
    }

    public MethodInfo(RuntimeValue instance, String methodName, RuntimeValue... runtimeValues) {
        this.instance = instance;
        this.methodName = methodName;
        this.runtimeValues = runtimeValues;
    }

    public MethodInfo(Structure structure, String methodName, RuntimeValue... runtimeValues) {
        this.structure = structure;
        this.methodName = methodName;
        this.runtimeValues = runtimeValues;
    }

    public RuntimeValue[] getRuntimeValues() {
        return runtimeValues;
    }

    public String getMethodName() {
        return methodName;
    }

    public Structure getStructure() {
        return structure;
    }

    public RuntimeValue getInstance() {
        return instance;
    }

    public boolean isStatic() {
        return structure != null;
    }

    public boolean isExternal() {
        return instance != null || structure != null;
    }

}
