package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PNumber extends Essence {

    static {
        Vial vial = new Vial("Number");
        vial.group("panda.lang");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PNumber((PNumber) particle.getValueOfFactor(0));
            }
        });
        vial.method(new Method("valueOf", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return null;
            }
        }));
        vial.method(new Method("toString", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PNumber number = particle.getInstance().getValue(particle);
                return new PString(number.toString());
            }
        }));
    }

    private final Number number;

    public PNumber(Number number) {
        this.number = number;
    }

    public PNumber(PNumber number) {
        this(number.getNumber());
    }

    public int intValue() {
        return number.intValue();
    }

    public Number getNumber() {
        return number;
    }

    @Override
    public Object getJavaValue() {
        return number;
    }

    @Override
    public String toString() {
        return number.toString();
    }

}
