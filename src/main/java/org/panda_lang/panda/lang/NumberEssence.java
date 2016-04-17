package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.essential.util.NumberType;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;

public class NumberEssence extends Essence {

    static {
        Vial vial = new Vial("Number");
        vial.group("panda.lang");
        vial.constructor(new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return null;
            }
        });
        vial.method(new Method("valueOf", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                return null;
            }
        }));
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                NumberEssence number = alice.getInstance().getValue(alice);
                return new StringEssence(number.toString());
            }
        }));
    }

    private Numeric number;

    protected NumberEssence(Numeric number) {
        this.number = number;
    }

    public NumberType getNumberType() {
        return number.getNumberType();
    }

    @Override
    public Object getJavaValue() {
        return number;
    }

    @Override
    public String toString() {
        return number.toString();
    }

}
