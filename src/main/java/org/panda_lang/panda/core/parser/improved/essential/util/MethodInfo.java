package org.panda_lang.panda.core.parser.improved.essential.util;

import org.panda_lang.panda.core.syntax.Parameter;

public class MethodInfo {

    private final String method;
    private final Parameter[] parameters;
    private Parameter instance;
    private String pseudoclass;

    public MethodInfo(String method, Parameter... parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    public MethodInfo(Parameter object, String method, Parameter... parameters) {
        this.instance = object;
        this.method = method;
        this.parameters = parameters;
    }

    public MethodInfo(String object, String method, Parameter... parameters) {
        this.pseudoclass = object;
        this.method = method;
        this.parameters = parameters;
    }

    public Parameter[] getParameters() {
        return parameters;
    }

    public String getMethod() {
        return method;
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
