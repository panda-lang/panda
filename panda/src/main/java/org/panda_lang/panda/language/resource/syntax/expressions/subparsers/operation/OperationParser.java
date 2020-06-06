/*
 * Copyright (c) 2020 Dzikoysk
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

import io.vavr.control.Option;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.interpreter.parser.Components;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.parser.expression.PandaExpressionParserFailure;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.pattern.OperationPatternResult;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.ConcatenationOperatorSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.LogicalOperatorSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.MathOperationSubparser;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

public final class OperationParser implements Parser {

    public Expression parse(Context context, ExpressionContext expressionContext, Snippet source) {
        return parse(context, expressionContext, source, OperationExpressionUtils.OPERATION_PATTERN.extract(source));
    }

    public Expression parse(Context context, ExpressionContext expressionContext, Snippet source, OperationPatternResult result) {
        return parse(context, Operation.of(context.getComponent(Components.EXPRESSION), context, expressionContext, result)).getOrElseThrow(() -> {
            throw new PandaExpressionParserFailure(expressionContext, source, "Unknown operation");
        });
    }

    public Option<Expression> parse(Context context, Operation operation) {
        return Match(operation).option(
                Case($(OperationUtils::isNumeric), () -> new MathOperationSubparser().parse(this, context, operation)),
                Case($(OperationUtils::isLogical), () -> new LogicalOperatorSubparser().parse(this, context, operation)),
                Case($(OperationUtils::isConcatenation), () -> new ConcatenationOperatorSubparser().parse(this, context, operation))
        );
    }

}