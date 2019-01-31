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
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.OperationSubparser;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.rpn.RPNOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.rpn.RPNOperationSupplier;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.logical.AndOperator;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.logical.EqualsOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.logical.NotEqualsOperation;
import org.panda_lang.panda.framework.language.resource.parsers.expression.callbacks.operation.subparsers.logical.OrOperation;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operator;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;
import org.panda_lang.panda.utilities.commons.collection.Maps;

import java.util.Map;

public class LogicalOperatorSubparser implements OperationSubparser {

    private static final Map<Operator, Integer> PRIORITIES = Maps.of(
            Operators.AND, 1,
            Operators.OR, 1,

            Operators.EQUAL_TO, 2,
            Operators.NOT_EQUAL_TO, 2
    );

    private static final Map<Operator, RPNOperationSupplier> ACTIONS = Maps.of(
            Operators.EQUAL_TO, new EqualsOperation(),
            Operators.NOT_EQUAL_TO, new NotEqualsOperation(),

            Operators.OR, new OrOperation(),
            Operators.AND, new AndOperator()
    );

    @Override
    public Expression parse(ParserData data, Operation operation) {
        RPNOperation rpn = RPNOperation.builder()
                .withData(data)
                .withOperation(operation)
                .withPriorities(PRIORITIES)
                .withActions(ACTIONS)
                .build();

        return rpn.rectify();
    }

}
