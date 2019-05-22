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

package org.panda_lang.panda.framework.language.resource.parsers.expression;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionResult;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.util.AbstractExpressionSubparserWorker;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.NegateLogicalExpressionCallback;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;

public class NegateExpressionSubparser implements ExpressionSubparser {

    @Override
    public ExpressionSubparserWorker createWorker() {
        return new NegateWorker();
    }

    @Override
    public String getSubparserName() {
        return "negate";
    }

    private static class NegateWorker extends AbstractExpressionSubparserWorker {

        @Override
        public @Nullable ExpressionResult next(ExpressionContext context) {
            if (!context.getCurrentRepresentation().contentEquals(Operators.NOT)) {
                return null;
            }

            Expression expression = context.getParser().parse(context.getData(), context.getDiffusedSource());
            return ExpressionResult.of(new NegateLogicalExpressionCallback(expression).toExpression());
        }

    }

}
