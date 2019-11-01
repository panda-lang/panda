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

package org.panda_lang.panda.language.interpreter.parser.expression.subparsers.operation.rpn;

import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.architecture.expression.ExpressionUtils;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.expression.ExpressionValueType;
import org.panda_lang.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.framework.language.architecture.expression.AbstractDynamicExpression;
import org.panda_lang.framework.language.architecture.expression.PandaExpression;
import org.panda_lang.utilities.commons.ObjectUtils;

import java.util.Map;
import java.util.Stack;

public class RPNOperationRectifier {

    private static final RPNOperationRectifier RECTIFIER = new RPNOperationRectifier();

    public Expression rectify(Map<Operator, RPNOperationSupplier> suppliers, Stack<?> elements) {
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

            if(a.getType() == ExpressionValueType.CONST && b.getType() == ExpressionValueType.CONST) {
                values.push(new PandaExpression(action.returnType(), action.get(null, ExpressionUtils.evaluateConstExpression(a), ExpressionUtils.evaluateConstExpression(b))));
                continue;
            }

            Expression expression = new PandaExpression(new AbstractDynamicExpression(action.returnType()) {
                @Override
                @SuppressWarnings("unchecked")
                public Object evaluate(ProcessStack stack, Object instance) throws Exception {
                    return action.get(stack, a.evaluate(stack, instance), b.evaluate(stack, instance));
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
