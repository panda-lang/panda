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

package org.panda_lang.panda.language.structure.scope.variable.assigners;

import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScopeInstance;

public class FieldAssigner implements Executable {

    private final Expression instanceExpression;
    private final int memoryIndex;
    private final Expression valueExpression;

    public FieldAssigner(Expression instanceExpression, int memoryIndex, Expression valueExpression) {
        this.instanceExpression = instanceExpression;
        this.memoryIndex = memoryIndex;
        this.valueExpression = valueExpression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        if (memoryIndex == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        Value instance = instanceExpression.getExpressionValue(branch);

        if (instance == null) {
            throw new PandaRuntimeException("Instance is not defined");
        }

        if (!(instance.getObject() instanceof ClassScopeInstance)) {
            throw new PandaRuntimeException("Cannot get field value of external object");
        }

        ClassScopeInstance pandaInstance = (ClassScopeInstance) instance.getObject();
        branch.instance(pandaInstance.toValue());

        Value value = valueExpression.getExpressionValue(branch);
        pandaInstance.getFieldValues()[memoryIndex] = value;
    }

    @Override
    public String toString() {
        return instanceExpression.getReturnType().getClassName() + "@f_memory[" + memoryIndex + "] << " + valueExpression.toString();
    }

}