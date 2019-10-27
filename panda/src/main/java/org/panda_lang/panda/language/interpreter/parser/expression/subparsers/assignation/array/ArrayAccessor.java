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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers.assignation.array;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.language.architecture.expression.DynamicExpression;
import org.panda_lang.framework.language.architecture.prototype.array.ArrayPrototype;

public final class ArrayAccessor implements DynamicExpression {

    private final Prototype type;
    private final Expression instanceExpression;
    private final Expression indexExpression;

    public ArrayAccessor(Expression instanceExpression, Expression indexExpression) {
        if (!(instanceExpression.getReturnType() instanceof ArrayPrototype)) {
            throw new RuntimeException("Array required");
        }

        this.type = ((ArrayPrototype) instanceExpression.getReturnType()).getArrayType();
        this.instanceExpression = instanceExpression;
        this.indexExpression = indexExpression;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object evaluate(ProcessStack stack, Object instance) throws Exception {
        return getArrayInstance(stack, instance)[getIndex(stack, instance)];
    }

    public ArrayAssigner toAssignerExpression(Expression value) {
        return new ArrayAssigner(this, value);
    }

    public int getIndex(ProcessStack stack, Object instance) throws Exception {
        return indexExpression.evaluate(stack, instance);
    }

    public <T> T[] getArrayInstance(ProcessStack stack, Object instance) throws Exception {
        return instanceExpression.evaluate(stack, instance);
    }

    @Override
    public Prototype getReturnType() {
        return type;
    }

}
