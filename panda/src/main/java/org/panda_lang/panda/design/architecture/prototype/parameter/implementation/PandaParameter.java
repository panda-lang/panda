package org.panda_lang.panda.design.architecture.prototype.parameter.implementation;

import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.parameter.Parameter;
import org.panda_lang.panda.design.architecture.value.PandaVariable;
import org.panda_lang.panda.design.architecture.value.Variable;

public class PandaParameter implements Parameter {

    private final String parameterName;
    private final ClassPrototype parameterType;

    public PandaParameter(ClassPrototype parameterType, String parameterName) {
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    @Override
    public Variable toVariable(int nestingLevel) {
        return new PandaVariable(parameterType, parameterName, nestingLevel);
    }

    @Override
    public ClassPrototype getParameterType() {
        return parameterType;
    }

    @Override
    public String getParameterName() {
        return parameterName;
    }

}
