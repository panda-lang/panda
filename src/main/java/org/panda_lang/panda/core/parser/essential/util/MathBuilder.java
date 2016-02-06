package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.util.SimpleEntry;

import java.util.Stack;

public class MathBuilder
{

    public enum Type
    {
        OPERATOR,
        PARAMETER
    }

    private Stack<SimpleEntry<Type, Object>> stack;
    private SimpleEntry<Type, Object>[] values;
    private int i;

    public MathBuilder()
    {
        this.stack = new Stack<>();
        this.i = -1;
    }

    public void append(char c)
    {
        this.stack.push(new SimpleEntry<Type, Object>(Type.OPERATOR, c));
    }

    public void append(Factor factor)
    {
        this.stack.push(new SimpleEntry<Type, Object>(Type.PARAMETER, factor));
    }

    public void rewrite()
    {
        this.values = stack.toArray(new SimpleEntry[stack.size()]);
    }

    public Type next()
    {
        i++;
        return i > values.length ? null : values[i].getKey();
    }

    public char getOperator()
    {
        return (char) values[i].getValue();
    }

    public Factor getParameter()
    {
        return (Factor) values[i].getValue();
    }

    public int size()
    {
        return values.length;
    }

}
