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

package org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers;

import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.Operation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.OperationParser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.OperationSubparser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.rpn.RPNOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.rpn.RPNOperationSupplier;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.math.AdditionOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.math.DivisionOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.math.MultiplicationOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.math.SubtractionOperation;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.util.Map;

public class MathOperationSubparser implements OperationSubparser {

    private static final Map<Operator, Integer> PRIORITIES = Maps.of(
            Operators.ADDITION, 1,
            Operators.SUBTRACTION, 1,

            Operators.MULTIPLICATION, 2,
            Operators.DIVISION, 2
    );

    private static final Map<Operator, RPNOperationSupplier> ACTIONS = Maps.of(
            Operators.ADDITION, new AdditionOperation(),
            Operators.SUBTRACTION, new SubtractionOperation(),

            Operators.MULTIPLICATION, new MultiplicationOperation(),
            Operators.DIVISION, new DivisionOperation()
    );

    @Override
    public Expression parse(OperationParser parser, ParserData data, Operation operation) {
        RPNOperation rpn = RPNOperation.builder()
                .withData(data)
                .withOperation(operation)
                .withPriorities(PRIORITIES)
                .withActions(ACTIONS)
                .build();

        return rpn.rectify();
    }

}
