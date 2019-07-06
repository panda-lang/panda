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

import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.Frame;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.memory.MemoryContainer;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public class FieldAssigner extends AbstractAssigner<PrototypeField> {

    private final Expression valueExpression;

    public FieldAssigner(Accessor<PrototypeField> accessor, Expression valueExpression) {
        super(accessor);
        this.valueExpression = valueExpression;
    }

    @Override
    public void execute(Frame frame) {
        PrototypeField field = accessor.getVariable();

        if (field.isStatic()) {
            Value staticValue = valueExpression.evaluate(frame);

            if (!field.isNullable() && (staticValue == null || staticValue.isNull())) {
                throw new PandaRuntimeException("Cannot assign null to static field '" + field.getName() + "' without nullable modifier");
            }

            if (!field.isMutable() && field.getStaticValue() != null) {
                throw new PandaRuntimeException("Cannot change value of immutable static field '" + field.getName() + "'");
            }

            field.setStaticValue(staticValue);
            return;
        }

        MemoryContainer memory = accessor.fetchMemoryContainer(frame);
        Value value = valueExpression.evaluate(frame);

        if (value.isNull() && !field.isNullable()) {
            throw new PandaRuntimeException("Cannot assign null to field  '" + field.getName() + "' without nullable modifier");
        }

        if (!field.isMutable() && memory.get(accessor.getMemoryPointer()) != null) {
            throw new PandaRuntimeException("Cannot change value of immutable field '" + field.getName() + "'");
        }

        memory.set(accessor.getMemoryPointer(), value);
    }

    @Override
    public String toString() {
        return accessor.getVariable().getPrototype().getClassName() + "@f_memory[" + accessor.getMemoryPointer() + "] << " + valueExpression;
    }

}