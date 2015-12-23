package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.Vial;

import java.util.Arrays;

public class MethodInfo {

    private final String methodName;
    private final Parameter[] parameters;
    private Parameter instance;
    private Vial vial;

    public MethodInfo(String methodName, Parameter... parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public MethodInfo(Parameter instance, String methodName, Parameter... parameters) {
        this.instance = instance;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public MethodInfo(Vial vial, String methodName, Parameter... parameters) {
        this.vial = vial;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Vial getVial() {
        return vial;
    }

    public Parameter getInstance() {
        return instance;
    }

    public boolean isStatic() {
        return vial != null;
    }

    public boolean isExternal() {
        return instance != null || vial != null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MethodInfo{");
        sb.append("methodName='").append(methodName).append('\'');
        sb.append(", parameters=").append(Arrays.toString(parameters));
        sb.append(", instance=").append(instance);
        sb.append(", vial=").append(vial);
        sb.append('}');
        return sb.toString();
    }

}
