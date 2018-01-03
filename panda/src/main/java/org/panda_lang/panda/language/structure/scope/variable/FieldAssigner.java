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

package org.panda_lang.panda.language.structure.scope.variable;

import org.panda_lang.panda.core.structure.dynamic.Executable;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScopeInstance;

public class FieldAssigner implements Executable {

    private final int memoryIndex;
    private final Expression expression;

    public FieldAssigner(int memoryIndex, Expression expression) {
        this.memoryIndex = memoryIndex;
        this.expression = expression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        if (memoryIndex == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        Object instance = branch.getInstance();

        if (instance == null) {
            throw new PandaRuntimeException("Instance is not defined");
        }

        if (!(instance instanceof ClassScopeInstance)) {
            throw new PandaRuntimeException("Cannot get field value of external object");
        }

        ClassScopeInstance pandaInstance = (ClassScopeInstance) instance;
        pandaInstance.getFieldValues()[memoryIndex] = expression.getExpressionValue(branch);
    }

    @Override
    public String toString() {
        return "'f_memory'[" + memoryIndex + "] << " + expression.toString();
    }

}