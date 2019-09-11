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

package org.panda_lang.panda.language.architecture.dynamic.assigner;

import org.panda_lang.panda.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.language.runtime.PandaRuntimeException;

public class FieldAssigner extends AbstractAssigner<PrototypeField> {

    private final Expression valueExpression;

    public FieldAssigner(Accessor<PrototypeField> accessor, Expression valueExpression) {
        super(accessor);
        this.valueExpression = valueExpression;
    }

    @Override
    public void execute(Flow flow) {
        PrototypeField field = accessor.getVariable();

        if (field.isStatic()) {
            Object staticValue = valueExpression.evaluate(flow);

            if (!field.isNillable() && staticValue == null) {
                throw new PandaRuntimeException("Cannot assign null to static field '" + field.getName() + "' without nil modifier");
            }

            if (!field.isMutable() && field.getStaticValue() != null) {
                throw new PandaRuntimeException("Cannot change value of immutable static field '" + field.getName() + "'");
            }

            field.setStaticValue(staticValue);
            return;
        }

        MemoryContainer memory = accessor.fetchMemoryContainer(flow);
        Object value = valueExpression.evaluate(flow);

        if (value == null && !field.isNillable()) {
            throw new PandaRuntimeException("Cannot assign null to field  '" + field.getName() + "' without nil modifier");
        }

        if (!field.isMutable() && memory.get(accessor.getMemoryPointer()) != null) {
            throw new PandaRuntimeException("Cannot change value of immutable field '" + field.getName() + "'");
        }

        memory.set(accessor.getMemoryPointer(), value);
    }

    @Override
    public String toString() {
        return accessor.getVariable().getPrototype().getName() + "@f_memory[" + accessor.getMemoryPointer() + "] << " + valueExpression;
    }

}