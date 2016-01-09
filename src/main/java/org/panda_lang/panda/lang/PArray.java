package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.*;

public class PArray extends Essence {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Array");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new PArray(particle.getFactors());
            }
        });
        vial.method(new Method("size", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PArray array = particle.getValueOfInstance();
                return new PNumber(array.getArray().length);
            }
        }));
        vial.method(new Method("get", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PNumber number = particle.getValueOfFactor(0);
                PArray instance = particle.getValueOfInstance();
                return instance.get(number.intValue());
            }
        }));
    }

    private final Factor[] array;

    public PArray(Factor... values) {
        super(vial);
        this.array = values;
    }

    public Essence get(int i) {
        return i < array.length ? array[i].getValue() : new PNull();
    }

    public Factor[] getArray() {
        return array;
    }

    @Override
    public Object getJavaValue() {
        return array;
    }

    @Override
    public String toString() {
        return array.toString();
    }

}
