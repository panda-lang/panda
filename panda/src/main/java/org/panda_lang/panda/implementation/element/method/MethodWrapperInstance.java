/*
 * Copyright (c) 2015-2016 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
