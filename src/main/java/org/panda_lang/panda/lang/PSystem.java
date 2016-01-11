package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PSystem extends PObject {

    private static final Vial vial;

    static {
        vial = new Vial("System");
        vial.group("panda.lang");
        vial.method(new Method("print", new Executable() {
            @Override
            public Essence run(Particle particle) {
                System.out.println(particle.getValueOfFactor(0));
                return null;
            }
        }));
        vial.method(new Method("currentTimeMillis", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PNumber(System.currentTimeMillis());
            }
        }));
        vial.method(new Method("nanoTime", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PNumber(System.nanoTime());
            }
        }));
        vial.method(new Method("exit", new Executable() {
            @Override
            public Essence run(Particle particle) {
                System.exit(-1);
                return new PNumber(-1);
            }
        }));
    }

}

