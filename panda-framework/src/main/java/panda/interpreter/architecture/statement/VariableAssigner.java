/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.architecture.statement;

import panda.interpreter.architecture.dynamic.accessor.Accessor;
import panda.interpreter.architecture.dynamic.assigner.AbstractAssigner;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.source.Location;
import panda.interpreter.runtime.PandaRuntimeException;
import panda.interpreter.runtime.ProcessStack;

public final class VariableAssigner extends AbstractAssigner<Variable> {

    private final boolean initialize;
    private final Expression expression;

    public VariableAssigner(Location location, Accessor<Variable> accessor, boolean initialize, Expression expression) {
        super(location, accessor);
        this.initialize = initialize;
        this.expression = expression;
    }

    @Override
    public Object execute(ProcessStack stack, Object instance) throws Exception {
        Variable variable = accessor.getVariable();

        if (!initialize && !variable.isMutable()) {
            throw new PandaRuntimeException("Cannot change value of immutable variable '" + variable.getName() + "'");
        }

        Object value = expression.evaluate(stack, instance);

        if (value == null && !variable.isNillable()) {
            throw new PandaRuntimeException("Cannot assign null to variable '" + variable.getName() + "' without nil modifier");
        }

        return accessor.fetchMemoryContainer(stack, instance).set(accessor.getMemoryPointer(), value);
    }

    @Override
    public String toString() {
        return "'v_memory'[" + accessor.getMemoryPointer() + "] << " + expression;
    }

}
