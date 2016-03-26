package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;

public class BooleanEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Boolean");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Alice alice) {
                Essence essence = alice.getValueOfFactor(0);
                if (essence instanceof NullEssence) {
                    return new BooleanEssence(false);
                }
                else if (essence instanceof BooleanEssence) {
                    return new BooleanEssence(((BooleanEssence) essence).getBoolean());
                }
                else if (essence instanceof Numeric) {
                    byte value = ((Numeric) essence).getByte();
                    return new BooleanEssence(value != 0);
                }
                return new BooleanEssence(false);
            }
        });
    }

    private final boolean b;

    public BooleanEssence(boolean b) {
        this.b = b;
    }

    public boolean isTrue() {
        return b;
    }

    public boolean isFalse() {
        return !b;
    }

    public boolean getBoolean() {
        return b;
    }

    @Override
    public Object getJavaValue() {
        return b;
    }

    @Override
    public String toString() {
        return Boolean.toString(b);
    }

}
