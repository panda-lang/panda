package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PSystem extends PObject {

    static {
        Vial vial = new Vial("System");
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
                return new PLong(System.currentTimeMillis());
            }
        }));
        vial.method(new Method("nanoTime", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PLong(System.nanoTime());
            }
        }));
        vial.method(new Method("exit", new Executable() {
            @Override
            public Essence run(Particle particle) {
                System.exit(-1);
                return new PInt(-1);
            }
        }));
    }

}

