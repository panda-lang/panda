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

package org.panda_lang.panda.language.structure.general.expression.callbacks.memory;

import org.panda_lang.panda.framework.design.architecture.detach.Value;
import org.panda_lang.panda.framework.design.architecture.detach.ExecutableBranch;
import org.panda_lang.panda.design.runtime.PandaRuntimeException;
import org.panda_lang.panda.framework.design.architecture.detach.Expression;
import org.panda_lang.panda.framework.design.architecture.detach.ExpressionCallback;
import org.panda_lang.panda.language.structure.prototype.scope.ClassScopeInstance;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;

public class FieldExpressionCallback implements ExpressionCallback {

    private final Expression instanceExpression;
    private final PrototypeField field;
    private final int fieldIndex;

    public FieldExpressionCallback(Expression instanceExpression, PrototypeField field, int fieldIndex) {
        this.instanceExpression = instanceExpression;
        this.field = field;
        this.fieldIndex = fieldIndex;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        if (field.isStatic()) {
            return field.getStaticValue().getValue();
        }

        Value instance = instanceExpression.getExpressionValue(branch);

        if (instance == null) {
            throw new PandaRuntimeException("Instance is not defined");
        }

        branch.instance(instance);

        if (field.isNative()) {
            return field.getDefaultValue().getExpressionValue(branch);
        }

        if (!(instance.getObject() instanceof ClassScopeInstance)) {
            throw new PandaRuntimeException("Cannot get field value of external object");
        }

        ClassScopeInstance pandaInstance = (ClassScopeInstance) instance.getObject();
        Value value = pandaInstance.getFieldValues()[fieldIndex];

        if (value == null) {
            throw new PandaRuntimeException("Field '" + field.getName() + "' have not been initialized");
        }

        return value;
    }

}
