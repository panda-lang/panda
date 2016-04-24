package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.core.statement.Operator;

public class EqualityBuilder {

    private RuntimeValue one, other;
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

    public RuntimeValue getOther() {
        return other;
    }

    public void setOther(RuntimeValue other) {
        this.other = other;
    }

    public RuntimeValue getOne() {
        return one;
    }

    public void setOne(RuntimeValue one) {
        this.one = one;
    }

}
