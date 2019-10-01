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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers.assignation.variable;

import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.framework.language.architecture.dynamic.assigner.AbstractAssigner;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;

public class FieldAssigner extends AbstractAssigner<PrototypeField> {

    private final boolean initialize;
    private final Expression valueExpression;

    public FieldAssigner(Accessor<PrototypeField> accessor, boolean initialize, Expression valueExpression) {
        super(accessor);
        this.initialize = initialize;
        this.valueExpression = valueExpression;
    }

    @Override
    public Object execute(ProcessStack stack, Object instance) throws Exception {
        PrototypeField field = accessor.getVariable();

        if (!initialize && !field.isMutable()) {
            throw new PandaRuntimeException("Cannot change value of immutable field '" + field.getName() + "'");
        }

        Object value = valueExpression.evaluate(stack, instance);

        if (value == null && !field.isNillable()) {
            throw new PandaRuntimeException("Cannot assign null to field  '" + field.getName() + "' without nil modifier");
        }

        if (field.isStatic()) {
            field.setStaticValue(value);
            return value;
        }

        return accessor.fetchMemoryContainer(stack, instance).set(accessor.getMemoryPointer(), value);
    }

    @Override
    public String toString() {
        return accessor.getVariable().getReference().getName() + "assigner@f_memory[" + accessor.getMemoryPointer() + "] << " + valueExpression;
    }

}