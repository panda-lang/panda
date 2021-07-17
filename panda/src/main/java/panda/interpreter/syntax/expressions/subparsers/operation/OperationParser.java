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

package panda.interpreter.syntax.expressions.subparsers.operation;

import panda.interpreter.architecture.expression.Expression;
import panda.interpreter.parser.Context;
import panda.interpreter.parser.PandaParserFailure;
import panda.interpreter.parser.Parser;
import panda.interpreter.parser.expression.ExpressionContext;
import panda.interpreter.token.Snippet;
import panda.interpreter.syntax.expressions.subparsers.operation.pattern.OperationPatternResult;
import panda.interpreter.syntax.expressions.subparsers.operation.subparsers.BitwiseOperatorSubparser;
import panda.interpreter.syntax.expressions.subparsers.operation.subparsers.ConcatenationOperatorSubparser;
import panda.interpreter.syntax.expressions.subparsers.operation.subparsers.LogicalOperatorSubparser;
import panda.interpreter.syntax.expressions.subparsers.operation.subparsers.MathOperationSubparser;
import panda.std.Case;
import panda.std.Option;

public final class OperationParser implements Parser {

    @Override
    public String name() {
        return "operation";
    }

    public Expression parse(Context<?> context, ExpressionContext<?> expressionContext, Snippet source) {
        return parse(context, expressionContext, source, OperationExpressionUtils.OPERATION_PATTERN.extract(source));
    }

    public Expression parse(Context<?> context, ExpressionContext<?> expressionContext, Snippet source, OperationPatternResult result) {
        return parse(context, Operation.of(context.getExpressionParser(), context, expressionContext, result)).orThrow(() -> {
            throw new PandaParserFailure(expressionContext.toContext(), source, "Unknown operation");
        });
    }

    public Option<Expression> parse(Context<?> context, Operation operation) {
        return Option.of(operation).match(
                Case.of(OperationUtils::isLogical, o -> new LogicalOperatorSubparser().parse(this, context, operation)),
                Case.of(OperationUtils::isNumeric, o -> new MathOperationSubparser().parse(this, context, operation)),
                Case.of(OperationUtils::isBitwise, o -> new BitwiseOperatorSubparser().parse(this, context, operation)),
                Case.of(OperationUtils::isConcatenation, o -> new ConcatenationOperatorSubparser().parse(this, context, operation))
        );
    }

}