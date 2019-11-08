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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.Operation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationParser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.OperationSubparser;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn.RPNOperationSupplier;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.AndOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.EqualsToOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.GreaterThanOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.GreaterThanOrEqualsOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.LessThanOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.LessThanOrEqualsOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.NotEqualsToOperation;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.logical.OrOperation;
import org.panda_lang.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.utilities.commons.collection.Maps;

import java.util.Map;

public final class LogicalOperatorSubparser implements OperationSubparser {

    private static final int LOGICAL = 1;
    private static final int COMPARE = 2;

    private static final Map<Operator, Integer> PRIORITIES = Maps.of(
            Operators.AND, LOGICAL,
            Operators.OR, LOGICAL,

            Operators.EQUAL_TO, COMPARE,
            Operators.NOT_EQUAL_TO, COMPARE,

            Operators.GREATER_THAN, COMPARE,
            Operators.LESS_THAN, COMPARE,

            Operators.GREATER_THAN_OR_EQUAL_TO, COMPARE,
            Operators.LESS_THAN_OR_EQUAL_TO, COMPARE
    );

    private static final Map<Operator, RPNOperationSupplier> ACTIONS = Maps.of(
            Operators.EQUAL_TO, new EqualsToOperation(),
            Operators.NOT_EQUAL_TO, new NotEqualsToOperation(),

            Operators.OR, new OrOperation(),
            Operators.AND, new AndOperator(),

            Operators.GREATER_THAN, new GreaterThanOperator(),
            Operators.LESS_THAN, new LessThanOperator(),

            Operators.GREATER_THAN_OR_EQUAL_TO, new GreaterThanOrEqualsOperator(),
            Operators.LESS_THAN_OR_EQUAL_TO, new LessThanOrEqualsOperator()
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
