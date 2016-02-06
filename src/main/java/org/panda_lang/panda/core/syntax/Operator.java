package org.panda_lang.panda.core.syntax;

import java.util.ArrayList;
import java.util.Collection;

public enum Operator
{

    ADDITIVE(0, "+"),
    SUBTRACTION(1, "-"),
    MULTIPLICATION(2, "*"),
    DIVISION(3, "/"),
    POWER(4, "^"),
    REST(5, "%"),

    AND(10, "&&"),
    OR(11, "||"),
    EQUALS_TO(12, "=="),
    NOT_EQUALS_TO(13, "!="),
    GREATER_THAN(14, ">"),
    GREATER_THAN_OR_EQUAL_TO(15, ">="),
    LESS_THAN(16, "<"),
    LESS_THAN_OR_EQUAL_TO(17, "<="),

    BITWISE_AND(30, "&"),
    BITWISE_OR(31, "|"),
    BITWISE_XOR(32, "^|"),
    BITWISE_NOT(33, "~"),
    LEFT_SHIFT(34, "<<"),
    RIGHT_SHIFT(35, ">>"),
    ZERO_FILL_RIGHT_SHIFT(36, ">>>");

    private final int id;
    private final String operator;

    Operator(int id, String operator)
    {
        this.id = id;
        this.operator = operator;
    }

    public String getOperator()
    {
        return this.operator;
    }

    public int getPart()
    {
        return this.id / 10;
    }

    public int getID()
    {
        return this.id;
    }

    public static Collection<Operator> getOperators(int part)
    {
        Collection<Operator> operators = new ArrayList<>();
        for (Operator operator : values())
        {
            if (operator.getPart() == part)
            {
                operators.add(operator);
            }
        }
        return operators;
    }

    public static Operator getOperator(int id)
    {
        for (Operator operator : values())
        {
            if (operator.getID() == id)
            {
                return operator;
            }
        }
        return null;
    }

    public static Operator getOperator(String s)
    {
        for (Operator operator : values())
        {
            if (operator.getOperator().equals(s))
            {
                return operator;
            }
        }
        return null;
    }

}
