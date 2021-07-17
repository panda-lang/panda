/*
 * Copyright (c) 2021 dzikoysk
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

package panda.interpreter.syntax.expressions.subparsers.operation.rpn;

import panda.interpreter.PandaFrameworkException;
import panda.interpreter.architecture.expression.AbstractDynamicExpression;
import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.architecture.expression.ExpressionValueType;
import panda.interpreter.architecture.expression.PandaExpression;
import panda.interpreter.architecture.module.TypeLoader;
import panda.interpreter.parser.PandaParserException;
import panda.interpreter.resource.syntax.operator.Operator;
import panda.interpreter.runtime.ProcessStack;
import panda.utilities.ObjectUtils;

import java.util.Map;
import java.util.Stack;

public final class RPNOperationRectifier {

    private static final RPNOperationRectifier RECTIFIER = new RPNOperationRectifier();

    public Expression rectify(TypeLoader typeLoader, Map<Operator, RPNOperationSupplier<?>> suppliers, Stack<?> elements) {
        Stack<Expression> values = new Stack<>();

        for (Object element : elements) {
            if (!(element instanceof Operator)) {
                values.push((Expression) element);
                continue;
            }

            Operator operator = ObjectUtils.cast(Operator.class, element);
            RPNOperationSupplier<?> supplier = suppliers.get(operator);

            if (supplier == null) {
                throw new PandaFrameworkException("Unexpected or unsupported operator " + operator);
            }

            // inverted on stack
            Expression b = values.pop();
            Expression a = values.pop();
            RPNOperationAction<?> action = supplier.of(typeLoader, a, b);

            if (a.getExpressionType() == ExpressionValueType.CONST && b.getExpressionType() == ExpressionValueType.CONST) {
                try {
                    Object constValue = action.get(null, null);
                    values.push(new PandaExpression(action.returnType(typeLoader), constValue));
                } catch (Exception cause) {
                    throw new PandaParserException("Cannot evaluate static expression: " + cause.toString(), cause);
                }

                continue;
            }

            Expression expression = new PandaExpression(new AbstractDynamicExpression(action.returnType(typeLoader)) {
                @Override
                @SuppressWarnings("unchecked")
                public Object evaluate(ProcessStack stack, Object instance) throws Exception {
                    return action.get(stack, instance);
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
