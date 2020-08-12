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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers;

import org.panda_lang.language.architecture.expression.Expression;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.resource.syntax.operator.Operator;
import org.panda_lang.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.Operation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationParser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationSupplier;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.math.AdditionOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.math.DivisionOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.math.MultiplicationOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.math.SubtractionOperation;
import org.panda_lang.utilities.commons.collection.Maps;

import java.util.Map;

public final class MathOperationSubparser implements OperationSubparser {

    private static final Map<Operator, Integer> PRIORITIES = Maps.of(
            Operators.ADDITION, 1,
            Operators.SUBTRACTION, 1,

            Operators.MULTIPLICATION, 2,
            Operators.DIVISION, 2
    );

    private static final Map<Operator, RPNOperationSupplier<?>> ACTIONS = Maps.of(
            Operators.ADDITION, new AdditionOperation(),
            Operators.SUBTRACTION, new SubtractionOperation(),

            Operators.MULTIPLICATION, new MultiplicationOperation(),
            Operators.DIVISION, new DivisionOperation()
    );

    @Override
    public Expression parse(OperationParser parser, Context context, Operation operation) {
        RPNOperation rpn = RPNOperation.builder()
                .withData(context)
                .withOperation(operation)
                .withPriorities(PRIORITIES)
                .withActions(ACTIONS)
                .build();

        return rpn.rectify();
    }

}
