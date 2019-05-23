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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.callbacks;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.prototype.field.PrototypeField;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.FieldAccessor;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;

public class FieldExpressionCallback implements ExpressionCallback {

    private final Accessor<? extends PrototypeField> accessor;

    public FieldExpressionCallback(Accessor<? extends PrototypeField> accessor) {
        this.accessor = accessor;
    }

    public FieldExpressionCallback(Expression instanceExpression, PrototypeField field, int internalPointer) {
        this(new FieldAccessor(instanceExpression, field));
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        PrototypeField field = accessor.getVariable();

        if (field.isStatic()) {
            return field.getStaticValue().getValue();
        }

        if (field.isNative()) {
            return field.getDefaultValue().getExpressionValue(branch);
        }

        Value value = accessor.getValue(branch);

        if (value == null) {
            throw new PandaRuntimeException("Field '" + field.getName() + "' have not been initialized");
        }

        return value;
    }

    @Override
    public ClassPrototype getReturnType() {
        return accessor.getTypeReference().fetch();
    }

    @Override
    public Expression toExpression() {
        return new AccessorExpression(accessor, this);
    }

}
