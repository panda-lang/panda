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

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed.subparsers.operation.rpn;

import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.runtime.ExecutableBranch;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.architecture.value.PandaValue;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpressionCallback;
import org.panda_lang.panda.utilities.commons.ObjectUtils;

import java.util.Map;
import java.util.Stack;

class RPNOperationRectifier {

    private static final RPNOperationRectifier RECTIFIER = new RPNOperationRectifier();

    public Expression rectify(ParserData data, Map<Operator, RPNOperationSupplier> suppliers, Stack<Object> elements) {
        Stack<Expression> values = new Stack<>();

        for (Object element : elements) {
            if (!(element instanceof Operator)) {
                values.push((Expression) element);
                continue;
            }

            Operator operator = ObjectUtils.cast(Operator.class, element);
            RPNOperationSupplier supplier = suppliers.get(operator);

            if (supplier == null) {
                throw new PandaParserFailure("Unexpected or unsupported operator " + operator, data);
            }

            Expression a = values.pop();
            Expression b = values.pop();
            RPNOperationAction action = supplier.of(a, b);

            Expression expression = new PandaExpression(new PandaExpressionCallback(action.returnType()) {
                @Override
                public Value call(Expression expression, ExecutableBranch branch) {
                    return new PandaValue(getReturnType(), action.get(branch, a.getExpressionValue(branch), b.getExpressionValue(branch)));
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
