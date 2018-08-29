/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.dynamic.accessor;

import org.panda_lang.panda.framework.design.architecture.dynamic.ExecutableStatement;
import org.panda_lang.panda.framework.design.architecture.dynamic.ScopeInstance;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public class VariableAccessor extends ExecutableStatement {

    private final Variable variable;
    private final int internalPointer;
    private final Expression expression;

    public VariableAccessor(Variable variable, int internalPointer, Expression expression) {
        this.variable = variable;
        this.internalPointer = internalPointer;
        this.expression = expression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        if (internalPointer == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        Value value = expression.getExpressionValue(branch);
        ScopeInstance currentScope = branch.getCurrentScope();

        if (value == null) {
            throw new PandaRuntimeException("Cannot assign not existing value to variable '" + variable.getName() + "'");
        }

        if (value.isNull() && !variable.isNullable()) {
            throw new PandaRuntimeException("Cannot assign null to variable '" + variable.getName() + "' without nullable modifier");
        }

        if (!variable.isMutable() && currentScope.get(internalPointer) != null) {
            throw new PandaRuntimeException("Cannot change value of immutable variable '" + variable.getName() + "'");
        }

        currentScope.set(internalPointer, value);
    }

    @Override
    public String toString() {
        return "'v_memory'[" + internalPointer + "] << " + expression;
    }

}
