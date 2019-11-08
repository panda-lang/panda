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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.statement.Variable;
import org.panda_lang.framework.design.interpreter.source.SourceLocation;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.framework.language.architecture.dynamic.assigner.AbstractAssigner;

public final class VariableAssigner extends AbstractAssigner<Variable> {

    private final boolean initialize;
    private final Expression expression;

    public VariableAssigner(SourceLocation location, Accessor<Variable> accessor, boolean initialize, Expression expression) {
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
