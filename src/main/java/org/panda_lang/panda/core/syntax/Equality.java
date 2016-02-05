package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.essential.util.EqualityBuilder;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.lang.PBoolean;

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
            //TODO

            Numeric oneNumber = oneFactor.getValue(particle);
            Numeric otherNumber = otherFactor.getValue(particle);

            float one = oneNumber.getFloat();
            float other = otherNumber.getFloat();

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
