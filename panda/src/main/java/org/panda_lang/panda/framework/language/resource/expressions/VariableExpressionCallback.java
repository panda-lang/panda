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

package org.panda_lang.panda.framework.language.resource.expressions;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.architecture.value.Variable;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.Accessor;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.AccessorExpression;
import org.panda_lang.panda.framework.language.architecture.dynamic.accessor.VariableAccessor;

public class VariableExpressionCallback implements ExpressionCallback {

    private final Accessor<?> accessor;

    public VariableExpressionCallback(Variable variable, int internalPointer) {
        this(new VariableAccessor(variable, internalPointer));
    }

    public VariableExpressionCallback(Accessor<?> accessor) {
        this.accessor = accessor;
    }

    @Override
    public Value call(Expression expression, Flow flow) {
        return accessor.getValue(flow);
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
