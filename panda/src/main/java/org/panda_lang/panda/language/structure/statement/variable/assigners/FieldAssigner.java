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

import org.panda_lang.panda.framework.design.architecture.detach.Executable;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.detach.ExecutableBranch;
import org.panda_lang.panda.design.runtime.PandaRuntimeException;
import org.panda_lang.panda.framework.design.architecture.detach.Expression;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScopeInstance;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.StaticValue;

public class FieldAssigner implements Executable {

    private final PrototypeField field;
    private final Expression instanceExpression;
    private final Expression valueExpression;

    public FieldAssigner(Expression instanceExpression, PrototypeField field, Expression valueExpression) {
        this.instanceExpression = instanceExpression;
        this.field = field;
        this.valueExpression = valueExpression;
    }

    @Override
    public void execute(ExecutableBranch branch) {
        int memoryIndex = field.getFieldIndex();

        if (memoryIndex == -1) {
            throw new PandaRuntimeException("Invalid memory pointer, variable may not exist");
        }

        if (field.isStatic()) {
            StaticValue staticValue = StaticValue.of(valueExpression.getExpressionValue(branch));

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

        if (!field.isMutable() && pandaInstance.getVariables()[memoryIndex] != null) {
            throw new PandaRuntimeException("Cannot change value of immutable field '" + field.getName() + "'");
        }

        pandaInstance.getFieldValues()[memoryIndex] = value;
    }

    @Override
    public String toString() {
        return instanceExpression.getReturnType().getClassName() + "@f_memory[" + field.getFieldIndex() + "] << " + valueExpression.toString();
    }

}