package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.*;

public class PArray extends Essence {

    static {
        Vial vial = new Vial("Array");
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
                return new PInt(array.getArray().length);
            }
        }));
        vial.method(new Method("get", new Executable() {
            @Override
            public Essence run(Particle particle) {
                Numeric numeric = particle.getValueOfFactor(0);
                PArray instance = particle.getValueOfInstance();
                return instance.get(numeric.getInt());
            }
        }));
    }

    private final Factor[] array;

    public PArray(Factor... values) {
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
