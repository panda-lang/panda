package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.*;

public class PArray extends Essence {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Array");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PArray(particle.getParameters());
            }
        });
        vial.method(new Method("size", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PArray array = particle.getInstance().getValue(PArray.class);
                return new PNumber(array.getArray().length);
            }
        }));
        vial.method(new Method("get", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return null;
            }
        }));
    }

    private final Parameter[] array;

    public PArray(Parameter... values) {
        super(vial);
        this.array = values;
    }

    public Essence get(int i) {
        return i < array.length ? array[i].getValue() : new PNull();
    }

    public Parameter[] getArray() {
        return array;
    }

    @Override
    public String toString() {
        return array.toString();
    }

}
