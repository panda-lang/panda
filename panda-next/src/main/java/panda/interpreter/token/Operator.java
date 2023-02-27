package panda.interpreter.token;

import panda.interpreter.token.Operator.OperatorType;

import static panda.interpreter.token.Operator.OperatorGroup.ASSIGNATION;
import static panda.interpreter.token.Operator.OperatorGroup.BITWISE;
import static panda.interpreter.token.Operator.OperatorGroup.CREASE;
import static panda.interpreter.token.Operator.OperatorGroup.LOGICAL;
import static panda.interpreter.token.Operator.OperatorGroup.MATH;
import static panda.interpreter.token.Operator.OperatorGroup.OTHER;

public final class Operator extends Token<OperatorType> {

    public enum OperatorGroup {

        MATH,
        BITWISE,
        LOGICAL,
        CREASE,
        ASSIGNATION,
        OTHER

    }

    public enum OperatorType {

        ADDITION(MATH, "+"),
        SUBTRACTION(MATH, "-"),
        MULTIPLICATION(MATH, "*"),
        DIVISION(MATH, "/"),
        MODULO(MATH, "%"),

        BITWISE_NOT(BITWISE, "~", false),
        BITWISE_AND(BITWISE, "&"),
        BITWISE_OR(BITWISE, "|"),
        BITWISE_XOR(BITWISE, "^"),
        BITWISE_LEFT_SHIFT(BITWISE, "<<"),
        BITWISE_RIGHT_SHIFT(BITWISE, ">>"),

        LESS_THAN(LOGICAL, "<"),
        ANGLE_LEFT(LOGICAL, ">"),
        ANGLE_RIGHT(LOGICAL, "=="),
        NOT_EQUAL_TO(LOGICAL, "!="),
        GREATER_THAN_OR_EQUAL_TO(LOGICAL, ">="),
        LESS_THAN_OR_EQUAL_TO(LOGICAL, "<="),
        AND(LOGICAL, "&&"),
        OR(LOGICAL, "||"),
        NOT(LOGICAL, "!", false),

        INCREMENT(CREASE, "++", false),
        DECREMENT(CREASE, "--", false),

        ASSIGNMENT(ASSIGNATION, "=", false),
        ADDITION_ASSIGNMENT(ASSIGNATION, "+=", false),
        SUBTRACTION_ASSIGNMENT(ASSIGNATION, "-=", false),
        MULTIPLICATION_ASSIGNMENT(ASSIGNATION, "*=", false),
        DIVISION_ASSIGNMENT(ASSIGNATION, "/=", false),
        REMAINDER_ASSIGNMENT(ASSIGNATION, "%=", false),

        EROTEME(OTHER, "?"),
        COLON(OTHER, ":"),
        ARROW(OTHER, "->"),
        LAMBDA(OTHER, "=>");

        private static final OperatorType[] VALUES = values();

        private final OperatorGroup group;
        private final String operator;
        private final boolean conjunction;

        OperatorType(OperatorGroup group, String operator, boolean conjunction) {
            this.group = group;
            this.operator = operator;
            this.conjunction = conjunction;
        }

        OperatorType(OperatorGroup group, String operator) {
            this(group, operator, true);
        }

        public boolean isConjunction() {
            return conjunction;
        }

        public OperatorGroup getGroup() {
            return group;
        }

        public String getOperator() {
            return operator;
        }

        public static OperatorType[] getValues() {
            return VALUES;
        }

    }

    public Operator(OperatorType operatorType, int line, int caret) {
        super(TokenType.OPERATOR, operatorType, line, caret);
    }

    @Override
    public String toSourceString() {
        return getValue().getOperator();
    }

}
