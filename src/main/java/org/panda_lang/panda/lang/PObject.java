package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PObject extends Essence {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Object");
        vial.group("panda.lang");
        vial.extension(null);
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PObject();
            }
        });
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString(particle.getInstance().getValue().toString());
            }
        }));
    }

    public PObject() {
        super(vial);
    }

    public PObject(Vial vial) {
        super(vial);
    }

    public <T> T getMe(Class<T> clazz) {
        return (T) this;
    }

    public String getType() {
        return "Object";
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
