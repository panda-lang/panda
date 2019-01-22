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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation;

import org.panda_lang.panda.framework.design.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.Parser;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.pattern.vague.VagueResult;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.design.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.ConcatenationOperatorParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.MathOperationParser;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;

public class OperationExpressionParser implements Parser {

    public ExpressionCallback parse(ParserData data, Tokens source) {
        return parse(data, source, OperationExpressionUtils.OPERATION_EXTRACTOR.extract(source));
    }

    public ExpressionCallback parse(ParserData data, Tokens source, VagueResult result) {
        Operation operation = Operation.of(data.getComponent(PandaComponents.EXPRESSION), data, result);

        if (OperationUtils.isNumeric(operation)) {
            return new MathOperationParser().parse(data, operation);
        }

        if (OperationUtils.verifyOperator(operation, Operators.ADDITION)) {
            return new ConcatenationOperatorParser().parse(data, operation);
        }

        throw new PandaParserFailure("Unknown operation", data, source);
    }

}