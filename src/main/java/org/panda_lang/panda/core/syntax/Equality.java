package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.lang.PBoolean;
import org.panda_lang.panda.lang.PNumber;
import org.panda_lang.panda.util.EqualityBuilder;

public class Equality implements Executable {

    private EqualityBuilder equalityBuilder;

    public Equality(EqualityBuilder equalityBuilder) {
        this.equalityBuilder = equalityBuilder;
    }

    @Override
    public Essence run(Particle particle) {
        Operator operator = equalityBuilder.getOperator();
        boolean flag = false;

        if (operator == Operator.EQUALS_TO || operator == Operator.NOT_EQUALS_TO) {
            /* TODO
            flag = equalityBuilder.getOne().getValue().equals(equalityBuilder.getOther().getValue());
            */
            flag = (operator == Operator.EQUALS_TO) == flag;

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

        new PBoolean(flag);
        return null;
    }

}
