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
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise.AndBitwiseOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise.LeftShiftOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise.OrBitwiseOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise.RightShiftOperator;
import org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.subparsers.bitwise.XorBitwiseOperator;
import org.panda_lang.utilities.commons.collection.Maps;

import java.util.Map;

public final class BitwiseOperatorSubparser implements OperationSubparser {

    private static final int SHIFT = 1;
    private static final int OPERATOR = 2;

    private static final Map<Operator, Integer> PRIORITIES = Maps.of(
            Operators.BITWISE_LEFT_SHIFT, SHIFT,
            Operators.BITWISE_RIGHT_SHIFT, SHIFT,

            Operators.BITWISE_AND, OPERATOR,
            Operators.BITWISE_OR, OPERATOR,
            Operators.BITWISE_XOR, OPERATOR
    );

    private static final Map<Operator, RPNOperationSupplier<?>> ACTIONS = Maps.of(
            Operators.BITWISE_LEFT_SHIFT, new LeftShiftOperator(),
            Operators.BITWISE_RIGHT_SHIFT, new RightShiftOperator(),

            Operators.BITWISE_AND, new AndBitwiseOperator(),
            Operators.BITWISE_OR, new OrBitwiseOperator(),
            Operators.BITWISE_XOR, new XorBitwiseOperator()
    );

    @Override
    public Expression parse(OperationParser parser, Context<?> context, Operation operation) {
        RPNOperation rpn = RPNOperation.builder()
                .withData(context)
                .withOperation(operation)
                .withPriorities(PRIORITIES)
                .withActions(ACTIONS)
                .build();

        return rpn.rectify();
    }

}
