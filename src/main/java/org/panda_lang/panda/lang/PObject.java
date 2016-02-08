package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PObject extends Essence {

    static {
        Vial vial = new Vial("Object");
        vial.group("panda.lang");
        vial.extension(null);
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return particle.hasFactors() ? new PObject(particle.getFactor(0)) : new PObject();
            }
        });
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString(particle.getInstance().getValue().toString());
            }
        }));
    }

    private Object object;

    public PObject() {
    }

    public PObject(Object object) {
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getMe() {
        return (T) this;
    }

    @Override
    public Object getJavaValue() {
        return object;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
