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

package org.panda_lang.panda.framework.language.architecture.dynamic.accessor;

import org.panda_lang.panda.framework.design.architecture.dynamic.ExecutableStatement;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.prototype.ClassScopeInstance;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public class FieldAccessor extends ExecutableStatement {

    private final PrototypeField field;
    private final Expression instanceExpression;
    private final Expression valueExpression;

    public FieldAccessor(Expression instanceExpression, PrototypeField field, Expression valueExpression) {
        this.instanceExpression = instanceExpression;
        this.field = field;
        this.valueExpression = valueExpression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        int internalPointer = field.getFieldIndex();

        if (internalPointer == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        if (field.isStatic()) {
            StaticValue staticValue = PandaStaticValue.of(valueExpression.getExpressionValue(branch));

            if ((staticValue.getValue() == null || staticValue.getValue().isNull()) && !field.isNullable()) {
                throw new PandaRuntimeException("Cannot assign null to static field '" + field.getName() + "' without nullable modifier");
            }

            if (!field.isMutable() && field.getStaticValue() != null) {
                throw new PandaRuntimeException("Cannot change value of immutable static field '" + field.getName() + "'");
            }

            field.setStaticValue(staticValue);
            return;
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

        if (value.isNull() && !field.isNullable()) {
            throw new PandaRuntimeException("Cannot assign null to field  '" + field.getName() + "' without nullable modifier");
        }

        if (!field.isMutable() && pandaInstance.get(internalPointer) != null) {
            throw new PandaRuntimeException("Cannot change value of immutable field '" + field.getName() + "'");
        }

        pandaInstance.set(internalPointer, value);
    }

    @Override
    public String toString() {
        return instanceExpression.getReturnType().getClassName() + "@f_memory[" + field.getFieldIndex() + "] << " + valueExpression;
    }

}