package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Vial;

public class PString extends PObject {

    static {
        Vial vial = new Vial("String");
        vial.group("panda.lang");
        vial.constructor(new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PString(particle.getValueOfFactor(0).toString());
            }
        });
    }

    private final String string;

    public PString(String string) {
        this.string = string;
    }

    public PBoolean contains(PObject o) {
        return new PBoolean(string.contains(o.toString()));
    }

    public PString replace(PObject f, PObject t) {
        String from = f.toString();
        String to = t.toString();
        return new PString(string.replace(from, to));
    }

    @Override
    public Object getJavaValue() {
        return string;
    }

    @Override
    public String toString() {
        return string;
    }

}
