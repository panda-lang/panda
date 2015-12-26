package org.panda_lang.panda.core.syntax;

public enum Operator {

    ADDITIVE(0, "+"),
    SUBTRACTION(1, "-"),
    MULTIPLICATION(2, "*"),
    DIVISION(3, "/"),
    POWER(4, "^"),

    EQUALS_TO(10, "=="),
    NOT_EQUALS_TO(11, "!="),
    GREATER_THAN(12, ">"),
    GREATER_THAN_OR_EQUAL_TO(13, ">="),
    LESS_THAN(14, "<"),
    LESS_THAN_OR_EQUAL_TO(15, "<=");

    private final int id;
    private final String operator;

    Operator(int id, String operator) {
        this.id = id;
        this.operator = operator;
    }

    public String getOperator() {
        return this.operator;
    }

    public int getPart() {
        return this.id / 10;
    }

    public int getID() {
        return this.id;
    }

    public static Operator getOperator(int id) {
        for (Operator operator : values()) {
            if (operator.getID() == id) {
                return operator;
            }
        }
        return null;
    }

    public static Operator getOperator(String s) {
        for (Operator operator : values()) {
            if (operator.getOperator().equals(s)) {
                return operator;
            }
        }
        return null;
    }

}
