package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.core.statement.Operator;

public class EqualityBuilder {

    private Factor one, other;
    private Operator operator;

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(String s) {
        this.operator = Operator.getOperator(s);
    }

    public Factor getOther() {
        return other;
    }

    public void setOther(Factor other) {
        this.other = other;
    }

    public Factor getOne() {
        return one;
    }

    public void setOne(Factor one) {
        this.one = one;
    }

}
