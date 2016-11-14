package org.panda_lang.panda.implementation.element.parameter;

public class Parameter {

    private final String parameterType;
    private final String parameterName;

    public Parameter(String parameterType, String parameterName) {
        this.parameterType = parameterType;
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterType() {
        return parameterType;
    }

}
