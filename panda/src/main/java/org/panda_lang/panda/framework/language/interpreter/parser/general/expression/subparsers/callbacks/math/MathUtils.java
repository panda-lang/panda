package org.panda_lang.panda.framework.language.interpreter.parser.general.expression.subparsers.callbacks.math;

import org.panda_lang.panda.framework.design.interpreter.token.Token;
import org.panda_lang.panda.framework.language.resource.syntax.operator.Operators;

public class MathUtils {

    public static final Token[] MATH_OPERATORS = new Token[]{
            Operators.ADDITION,
            Operators.SUBTRACTION,
            Operators.DIVISION,
            Operators.MULTIPLICATION,

            Operators.BITWISE_AND,
            Operators.BITWISE_NOT,
            Operators.BITWISE_OR,
            Operators.BITWISE_XOR,
            Operators.BITWISE_LEFT_SHIFT,
            Operators.BITWISE_RIGHT_SHIFT
    };

}
