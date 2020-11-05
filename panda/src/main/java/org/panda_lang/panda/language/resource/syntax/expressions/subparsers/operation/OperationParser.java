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

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.language.interpreter.parser.expression.ExpressionContext;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.pattern.OperationPatternResult;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.ConcatenationOperatorSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.LogicalOperatorSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.MathOperationSubparser;
import org.panda_lang.utilities.commons.function.Case;
import org.panda_lang.utilities.commons.function.Option;

public final class OperationParser implements Parser {

    public Expression parse(Context<?> context, ExpressionContext expressionContext, Snippet source) {
        return parse(context, expressionContext, source, OperationExpressionUtils.OPERATION_PATTERN.extract(source));
    }

    public Expression parse(Context<?> context, ExpressionContext expressionContext, Snippet source, OperationPatternResult result) {
        return parse(context, Operation.of(context.getComponent(Components.EXPRESSION), context, expressionContext, result)).orThrow(() -> {
            throw new PandaParserFailure(expressionContext, source, "Unknown operation");
        });
    }

    public Option<Expression> parse(Context<?> context, Operation operation) {
        return Option.of(operation).match(
                Case.of(OperationUtils::isNumeric, o -> new MathOperationSubparser().parse(this, context, operation)),
                Case.of(OperationUtils::isLogical, o -> new LogicalOperatorSubparser().parse(this, context, operation)),
                Case.of(OperationUtils::isConcatenation, o -> new ConcatenationOperatorSubparser().parse(this, context, operation))
        );
    }

}