package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;

public class PBoolean extends PObject {

    static {
        Vial vial = new Vial("Boolean");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                Essence essence = particle.getValueOfFactor(0);
                if (essence instanceof PNull) {
                    return new PBoolean(false);
                } else if (essence instanceof PBoolean) {
                    return new PBoolean(((PBoolean) essence).getBoolean());
                } else if (essence instanceof Numeric) {
                    byte value = ((Numeric) essence).getByte();
                    return new PBoolean(value != 0);
                }
                return new PBoolean(false);
            }
        });
    }

    private final boolean b;

    public PBoolean(boolean b) {
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
