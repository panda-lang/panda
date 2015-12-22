package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Method;
import org.panda_lang.panda.core.syntax.Vial;

public class PSystem extends PObject {

    private static final Vial vial;

    static {
        vial = VialCenter.initializeVial("System");
        vial.method(new Method("print", new Executable() {
            @Override
            public Essence run(Particle particle) {
                System.out.println(particle.getValue(0));
                return null;
            }
        }));

        /*
        // Register object
        ObjectScheme os = new ObjectScheme(PSystem.class, "System");
        // Static method: print
        os.registerMethod(new MethodScheme("print", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                System.out.println(parameters[0].getValue());
                return null;
            }
        }));
        // Static method: exit
        os.registerMethod(new MethodScheme("exit", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... args) {
                System.exit(-1);
                return null;
            }
        }));
        // Static method: currentTimeMillis
        os.registerMethod(new MethodScheme("currentTimeMillis", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... args) {
                return new PNumber(System.currentTimeMillis());
            }
        }));
        // Static method: nanoTime
        os.registerMethod(new MethodScheme("nanoTime", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... args) {
                return new PNumber(System.nanoTime());
            }
        }));
        */

    }

}

