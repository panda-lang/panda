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

package panda.interpreter.architecture.type.member.field;

import panda.interpreter.architecture.dynamic.accessor.Accessor;
import panda.interpreter.architecture.dynamic.assigner.AbstractAssigner;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.source.Location;
import panda.interpreter.runtime.PandaRuntimeException;
import panda.interpreter.runtime.ProcessStack;

public final class FieldAssigner extends AbstractAssigner<TypeField> {

    private final boolean initialize;
    private final Expression valueExpression;

    public FieldAssigner(Location location, Accessor<TypeField> accessor, boolean initialize, Expression valueExpression) {
        super(location, accessor);
        this.initialize = initialize;
        this.valueExpression = valueExpression;
    }

    @Override
    public Object execute(ProcessStack stack, Object instance) throws Exception {
        TypeField field = accessor.getVariable();

        if (!initialize && !field.isMutable()) {
            throw new PandaRuntimeException("Cannot change value of immutable field '" + field.getSimpleName() + "'");
        }

        Object value = valueExpression.evaluate(stack, instance);

        if (value == null && !field.isNillable()) {
            throw new PandaRuntimeException("Cannot assign null to field  '" + field.getSimpleName() + "' without nil modifier");
        }

        if (field.isStatic()) {
            field.setStaticValue(() -> value);
            return value;
        }

        return accessor.fetchMemoryContainer(stack, instance).set(accessor.getMemoryPointer(), value);
    }

    @Override
    public String toString() {
        return accessor.getVariable().getReturnType() + "assigner@f_memory[" + accessor.getMemoryPointer() + "] << " + valueExpression;
    }

}