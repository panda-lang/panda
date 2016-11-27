package org.panda_lang.panda.implementation.element.method;

import org.panda_lang.framework.runtime.ExecutableBridge;
import org.panda_lang.framework.structure.*;

public class MethodWrapperInstance implements WrapperInstance {

    private final MethodWrapper methodWrapper;
    private final Object[] variables;

    public MethodWrapperInstance(MethodWrapper methodWrapper) {
        this.methodWrapper = methodWrapper;
        this.variables = new Object[methodWrapper.getFieldStatements().length];
    }

    @Override
    public void execute(ExecutableBridge executionInfo) {
        Value[] parameters = executionInfo.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            variables[i] = parameters[i].getValue();
        }

        for (StatementCell statementCell : methodWrapper.getStatementCells()) {
            if (!statementCell.isExecutable()) {
                continue;
            }

            Executable executable = (Executable) statementCell.getStatement();
            executionInfo.call(executable);
        }
    }

    @Override
    public Object[] getVariables() {
        return variables;
    }

    @Override
    public Wrapper getWrapper() {
        return methodWrapper;
    }

}
