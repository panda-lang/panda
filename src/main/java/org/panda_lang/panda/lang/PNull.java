package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PNull extends Essence {

    private static final Vial vial;

    static {
        vial = VialCenter.initializeVial("null");
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString("null");
            }
        }));
    }

    public PNull() {
        super(vial);
    }

    @Override
    public Object getJavaValue() {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }

}
