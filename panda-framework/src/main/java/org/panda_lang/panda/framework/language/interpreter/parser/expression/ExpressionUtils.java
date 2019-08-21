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

package org.panda_lang.panda.framework.language.interpreter.parser.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.language.runtime.expression.PandaExpression;

public class ExpressionUtils {

    public static @Nullable Object evaluate(Flow flow, @Nullable Expression expression) {
        return expression != null ? expression.evaluate(flow) : null;
    }

    public static Object[] getValues(Flow flow, Expression... expressions) {
        Object[] values = new Object[expressions.length];

        for (int i = 0; i < values.length; i++) {
            Expression expression = expressions[i];
            values[i] = expression.evaluate(flow);
        }

        return values;
    }

    public static ClassPrototype[] toTypes(Expression... expressions) {
        ClassPrototype[] prototypes = new ClassPrototype[expressions.length];

        for (int i = 0; i < prototypes.length; i++) {
            Expression expression = expressions[i];
            prototypes[i] = expression.getReturnType();
        }

        return prototypes;
    }

    public static Expression toExpression(ClassPrototype type, Object value) {
        return new PandaExpression(type, value);
    }

    public static ExpressionResult toExpressionResult(ClassPrototype type, Object value) {
        return ExpressionResult.of(toExpression(type, value));
    }

}
