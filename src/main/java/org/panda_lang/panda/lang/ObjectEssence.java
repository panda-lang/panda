package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class ObjectEssence extends Essence {

    static {
        Vial vial = new Vial("Object");
        vial.group("panda.lang");
        vial.extension(null);
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return particle.hasFactors() ? new ObjectEssence(particle.getFactor(0)) : new ObjectEssence();
            }
        });
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new StringEssence(particle.getInstance().getValue(particle).toString());
            }
        }));
    }

    private Object object;

    public ObjectEssence() {
    }

    public ObjectEssence(Object object) {
        this.object = object;
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
