package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.lang.PObject;
import org.panda_lang.panda.util.EqualityBuilder;

public class Equality implements IExecutable {

    private EqualityBuilder equalityBuilder;

    public Equality(EqualityBuilder equalityBuilder) {
        this.equalityBuilder = equalityBuilder;
    }

    @Override
    public PObject run(Parameter instance, Parameter... parameters) {
        Operator operator = equalityBuilder.getOperator();
        boolean flag = false;

        if (operator == Operator.EQUALS_TO || operator == Operator.NOT_EQUALS_TO) {
            flag = equalityBuilder.getOne().getValue().equals(equalityBuilder.getOther().getValue());
            flag = operator == Operator.EQUALS_TO ? flag : !flag;

        } else {
            int one = PNumber.getNumberValue(equalityBuilder.getOne()).intValue();
            int other = PNumber.getNumberValue(equalityBuilder.getOther()).intValue();

            switch (equalityBuilder.getOperator()) {
                case GREATER_THAN:
                    flag = one > other;
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    flag = one >= other;
                    break;
                case LESS_THAN:
                    flag = one < other;
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    flag = one <= other;
                    break;
                default:
                    break;
            }
        }

        return new PBoolean(flag);
    }

}
