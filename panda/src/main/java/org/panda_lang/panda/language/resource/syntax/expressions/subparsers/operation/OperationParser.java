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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.language.interpreter.pattern.progressive.ProgressivePatternResult;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.ConcatenationOperatorSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.LogicalOperatorSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.MathOperationSubparser;

public final class OperationParser implements Parser {

    public Expression parse(Context context, ExpressionContext expressionContext, Snippet source) {
        return parse(context, expressionContext, source, OperationExpressionUtils.OPERATION_PATTERN.extract(source));
    }

    public Expression parse(Context context, ExpressionContext expressionContext, Snippet source, ProgressivePatternResult result) {
        Expression expression = parse(context, Operation.of(context.getComponent(Components.EXPRESSION), context, expressionContext, result));

        if (expression == null) {
            throw new PandaExpressionParserFailure(expressionContext, source, "Unknown operation");
        }

        return expression;
    }

    public @Nullable Expression parse(Context context, Operation operation) {
        if (OperationUtils.isNumeric(operation)) {
            return new MathOperationSubparser().parse(this, context, operation);
        }

        if (OperationUtils.isLogical(operation)) {
            return new LogicalOperatorSubparser().parse(this, context, operation);
        }

        if (OperationUtils.isConcatenation(operation)) {
            return new ConcatenationOperatorSubparser().parse(this, context, operation);
        }

        return null;
    }

}