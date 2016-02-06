package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.Operator;

public class EqualityBuilder
{

    private Factor one, other;
    private Operator operator;

    public void setOne(Factor one)
    {
        this.one = one;
    }

    public void setOther(Factor other)
    {
        this.other = other;
    }

    public void setOperator(Operator operator)
    {
        this.operator = operator;
    }

    public void setOperator(String s)
    {
        this.operator = Operator.getOperator(s);
    }

    public Operator getOperator()
    {
        return operator;
    }

    public Factor getOther()
    {
        return other;
    }

    public Factor getOne()
    {
        return one;
    }

}
