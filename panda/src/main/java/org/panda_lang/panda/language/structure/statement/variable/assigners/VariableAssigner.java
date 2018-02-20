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

package org.panda_lang.panda.language.structure.statement.variable.assigners;

import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.core.structure.dynamic.ScopeInstance;
import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.general.expression.Expression;

public class VariableAssigner implements Executable {

    private final int memoryIndex;
    private final Expression expression;

    public VariableAssigner(int memoryIndex, Expression expression) {
        this.memoryIndex = memoryIndex;
        this.expression = expression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        if (memoryIndex == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        Value value = expression.getExpressionValue(branch);
        ScopeInstance currentScope = branch.getCurrentScope();

        currentScope.getVariables()[memoryIndex] = value;
    }

    @Override
    public String toString() {
        return "'v_memory'[" + memoryIndex + "] << " + expression.toString();
    }

}
