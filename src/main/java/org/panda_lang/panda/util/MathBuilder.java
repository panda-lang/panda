package org.panda_lang.panda.util;

import org.panda_lang.panda.core.syntax.Factor;

import java.util.Stack;

public class MathBuilder {

    public enum Type {
        OPERATOR,
        PARAMETR;
    }

    private Stack<SimpleEntry<Type, Object>> stack;
    private SimpleEntry<Type, Object>[] values;
    private int i;

    public MathBuilder() {
        this.stack = new Stack<>();
        this.i = -1;
    }

    public void append(char c) {
        this.stack.push(new SimpleEntry<Type, Object>(Type.OPERATOR, c));
    }

    public void append(Factor factor) {
        this.stack.push(new SimpleEntry<Type, Object>(Type.PARAMETR, factor));
    }

    public void rewrite() {
        this.values = stack.toArray(new SimpleEntry[stack.size()]);
    }

    public Type next() {
        i++;
        return i > values.length ? null : values[i].getKey();
    }

    public char getOperator() {
        return (char) values[i].getValue();
    }

    public Factor getParametr() {
        return (Factor) values[i].getValue();
    }

    public int size() {
        return values.length;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (SimpleEntry<Type, Object> entry : values) {
            node.append("[" + entry.getKey() + ": ");
            node.append((entry.getKey() == Type.OPERATOR ? entry.getValue() : ((Factor) entry.getValue()).getValue()) + "]");
        }
        return node.toString();
    }

}
