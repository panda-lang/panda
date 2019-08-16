/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.framework.language.architecture.dynamic.assigner;

import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public class VariableAssigner extends AbstractAssigner<Variable> {

    private final Expression expression;

    public VariableAssigner(Accessor<Variable> accessor, Expression expression) {
        super(accessor);
        this.expression = expression;
    }

    @Override
    public void execute(Flow flow) {
        Variable variable = accessor.getVariable();
        Value value = expression.evaluate(flow);

        if (value == null) {
            throw new PandaRuntimeException("Cannot assign not existing value to variable '" + variable.getName() + "'");
        }

        if (value.isNull() && !variable.isNillable()) {
            throw new PandaRuntimeException("Cannot assign null to variable '" + variable.getName() + "' without nil modifier");
        }

        MemoryContainer memory = accessor.fetchMemoryContainer(flow);

        if (!variable.isMutable() && memory.get(accessor.getMemoryPointer()) != null) {
            throw new PandaRuntimeException("Cannot change value of immutable variable '" + variable.getName() + "'");
        }

        memory.set(accessor.getMemoryPointer(), value);
    }

    @Override
    public String toString() {
        return "'v_memory'[" + accessor.getMemoryPointer() + "] << " + expression;
    }

}
