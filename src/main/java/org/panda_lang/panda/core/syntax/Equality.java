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
        Factor oneFactor = equalityBuilder.getOne();
        Factor otherFactor = equalityBuilder.getOther();
        boolean flag = false;

        if (operator == Operator.EQUALS_TO || operator == Operator.NOT_EQUALS_TO) {
            flag = oneFactor.getValue(particle).equals(otherFactor.getValue(particle));
            flag = (operator == Operator.EQUALS_TO) == flag;

        } else {
            PNumber oneNumber = oneFactor.getValue(particle);
            PNumber otherNumber = otherFactor.getValue(particle);

            float one = oneNumber.getNumber().floatValue();
            float other = otherNumber.getNumber().floatValue();

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
