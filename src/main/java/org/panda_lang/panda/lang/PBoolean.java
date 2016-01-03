package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Vial;

public class PBoolean extends PObject {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Boolean");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                Essence essence = particle.get(0).getValue();
                if (essence instanceof PNull) {
                    return new PBoolean(false);
                } else if (essence instanceof PBoolean) {
                    return new PBoolean(((PBoolean) essence).getBoolean());
                } else if (essence instanceof PNumber) {
                    Number number = ((PNumber) essence).getNumber();
                    return new PBoolean(number.intValue() != 0);
                }
                return new PBoolean(false);
            }
        });
    }

    private final boolean b;

    public PBoolean(boolean b) {
        super(vial);
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
    public String getType() {
        return "Boolean";
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
