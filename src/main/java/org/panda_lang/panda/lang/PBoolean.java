package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Vial;

public class PBoolean extends PObject {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Boolean");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return particle.get(0).getValue();
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
    public String toString() {
        return Boolean.toString(b);
    }

}
