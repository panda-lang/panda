package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.*;

public class PNumber extends Essence {

    private final static Vial vial;

    static {
        vial = VialCenter.initializeVial("Number");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return null;
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
                PNumber number = particle.getInstance().getValue(PNumber.class);
                return new PString(number.toString());
            }
        }));
    }

    private final Number number;

    public PNumber(Number number) {
        super(vial);
        this.number = number;
    }

    public Number getNumber() {
        return number;
    }

    @Override
    public String getType() {
        return "Number";
    }

    @Override
    public String toString() {
        return number.toString();
    }

    public static Number getNumberValue(Parameter parameter) {
        Essence value = parameter.getValue();
        if (value instanceof PNumber) {
            return ((PNumber) value).getNumber();
        }
        return 0;
    }

}
