package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Vial;

public class MethodInfo {

    private final String methodName;
    private final RuntimeValue[] runtimeValues;
    private RuntimeValue instance;
    private Vial vial;

    public MethodInfo(String methodName, RuntimeValue... runtimeValues) {
        this.methodName = methodName;
        this.runtimeValues = runtimeValues;
    }

    public MethodInfo(RuntimeValue instance, String methodName, RuntimeValue... runtimeValues) {
        this.instance = instance;
        this.methodName = methodName;
        this.runtimeValues = runtimeValues;
    }

    public MethodInfo(Vial vial, String methodName, RuntimeValue... runtimeValues) {
        this.vial = vial;
        this.methodName = methodName;
        this.runtimeValues = runtimeValues;
    }

    public RuntimeValue[] getRuntimeValues() {
        return runtimeValues;
    }

    public String getMethodName() {
        return methodName;
    }

    public Vial getVial() {
        return vial;
    }

    public RuntimeValue getInstance() {
        return instance;
    }

    public boolean isStatic() {
        return vial != null;
    }

    public boolean isExternal() {
        return instance != null || vial != null;
    }

}
