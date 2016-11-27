package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.framework.structure.Wrapper;
import org.panda_lang.framework.structure.WrapperInstance;
import org.panda_lang.panda.implementation.element.field.FieldStatement;
import org.panda_lang.panda.implementation.element.parameter.Parameter;
import org.panda_lang.panda.implementation.structure.AbstractContainer;

import java.util.List;

public class MethodWrapper extends AbstractContainer implements Wrapper {

    private final int wrapperID;
    private final String methodName;
    private final List<Parameter> parameters;
    private final FieldStatement[] fieldStatements;

    public MethodWrapper(int wrapperID, String methodName, List<Parameter> parameters, List<FieldStatement> fieldStatements) {
        this.wrapperID = wrapperID;
        this.methodName = methodName;
        this.parameters = parameters;
        this.fieldStatements = fieldStatements.toArray(new FieldStatement[fieldStatements.size()]);
    }

    @Override
    public WrapperInstance createInstance() {
        return new MethodWrapperInstance(this);
    }

    public FieldStatement[] getFieldStatements() {
        return fieldStatements;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String getName() {
        return methodName;
    }

    @Override
    public int getID() {
        return wrapperID;
    }

}
