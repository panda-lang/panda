package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PNull extends Essence {

    static {
        Vial vial = new Vial("null");
        vial.group("panda.lang");
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString("null");
            }
        }));
    }

    public PNull() {
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
