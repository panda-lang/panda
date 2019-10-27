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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.architecture.prototype.PrototypeField;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.framework.language.architecture.expression.DynamicExpression;
import org.panda_lang.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.language.interpreter.parser.expression.subparsers.assignation.variable.FieldAccessor;

final class FieldExpression implements DynamicExpression {

    private final Accessor<? extends PrototypeField> accessor;

    public FieldExpression(Accessor<? extends PrototypeField> accessor) {
        this.accessor = accessor;
    }

    public FieldExpression(Expression instanceExpression, PrototypeField field) {
        this(new FieldAccessor(instanceExpression, field));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        PrototypeField field = accessor.getVariable();

        if (field.isStatic()) {
            return field.getStaticValue();
        }

        if (field.isNative()) {
            return field.getDefaultValue().evaluate(stack, instance);
        }

        Object value = accessor.getValue(stack, instance);

        if (value == null) {
            throw new PandaRuntimeException("Field " + field + " has not been initialized");
        }

        return value;
    }

    @Override
    public Prototype getReturnType() {
        return accessor.getTypePrototype();
    }

    @Override
    public Expression toExpression() {
        return new AccessorExpression(accessor, this);
    }

}
