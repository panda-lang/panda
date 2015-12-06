package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.syntax.Parameter;

public class MethodInfo {

    private final String methodName;
    private final Parameter[] parameters;
    private Parameter instance;
    private String pseudoclass;

    public MethodInfo(String methodName, Parameter... parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public MethodInfo(Parameter object, String methodName, Parameter... parameters) {
        this.instance = object;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public MethodInfo(String object, String methodName, Parameter... parameters) {
        this.pseudoclass = object;
        this.methodName = methodName;
        this.parameters = parameters;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getPseudoclass() {
        return pseudoclass;
    }

    public Parameter getInstance() {
        return instance;
    }

    public boolean isStatic() {
        return pseudoclass != null;
    }

    public boolean isExternal() {
        return instance != null || pseudoclass != null;
    }

}
