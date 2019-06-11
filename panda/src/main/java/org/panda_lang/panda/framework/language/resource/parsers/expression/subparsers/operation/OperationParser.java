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

package org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.operation;

import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.pattern.progressive.ProgressivePatternResult;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.operation.subparsers.ConcatenationOperatorSubparser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.operation.subparsers.LogicalOperatorSubparser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.subparsers.operation.subparsers.MathOperationSubparser;

public class OperationParser implements Parser {

    public Expression parse(ParserData data, Snippet source) {
        return parse(data, source, OperationExpressionUtils.OPERATION_PATTERN.extract(source));
    }

    public Expression parse(ParserData data, Snippet source, ProgressivePatternResult result) {
        Expression expression = parse(data, Operation.of(data.getComponent(UniversalComponents.EXPRESSION), data, result));

        if (expression == null) {
            throw PandaParserFailure.builder("Unknown operation", data)
                    .withStreamOrigin(source)
                    .build();
        }

        return expression;
    }

    public Expression parse(ParserData data, Operation operation) {
        if (OperationUtils.isNumeric(operation)) {
            return new MathOperationSubparser().parse(this, data, operation);
        }

        if (OperationUtils.isLogical(operation)) {
            return new LogicalOperatorSubparser().parse(this, data, operation);
        }

        if (OperationUtils.isConcatenation(operation)) {
            return new ConcatenationOperatorSubparser().parse(this, data, operation);
        }

        return null;
    }

}