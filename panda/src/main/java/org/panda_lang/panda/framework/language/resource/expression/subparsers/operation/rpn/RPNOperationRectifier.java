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

package org.panda_lang.panda.framework.language.resource.expression.subparsers.operation.rpn;

import org.panda_lang.panda.framework.PandaFrameworkException;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.runtime.expression.PandaDynamicExpression;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

import java.util.Map;
import java.util.Stack;

class RPNOperationRectifier {

    private static final RPNOperationRectifier RECTIFIER = new RPNOperationRectifier();

    public Expression rectify(Map<Operator, RPNOperationSupplier> suppliers, Stack<Object> elements) {
        Stack<Expression> values = new Stack<>();

        for (Object element : elements) {
            if (!(element instanceof Operator)) {
                values.push((Expression) element);
                continue;
            }

            Operator operator = ObjectUtils.cast(Operator.class, element);
            RPNOperationSupplier supplier = suppliers.get(operator);

            if (supplier == null) {
                throw new PandaFrameworkException("Unexpected or unsupported operator " + operator);
            }

            // inversed on stack
            Expression b = values.pop();
            Expression a = values.pop();
            RPNOperationAction<?, ?, ?> action = supplier.of(a, b);

            Expression expression = new PandaExpression(new PandaDynamicExpression(action.returnType()) {
                @Override
                @SuppressWarnings("unchecked")
                public Object call(Expression expression, Flow flow) {
                    return action.get(flow, a.evaluate(flow), b.evaluate(flow));
                }
            });

            values.push(expression);
        }

        return values.pop();
    }

    public static RPNOperationRectifier getInstance() {
        return RECTIFIER;
    }

}
