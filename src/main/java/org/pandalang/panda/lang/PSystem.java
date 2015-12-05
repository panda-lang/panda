package org.pandalang.panda.lang;

import org.pandalang.panda.core.scheme.MethodScheme;
import org.pandalang.panda.core.scheme.ObjectScheme;
import org.pandalang.panda.core.syntax.IExecutable;
import org.pandalang.panda.core.syntax.Parameter;

public class PSystem extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PSystem.class, "System");
        // Static method: print
        os.registerMethod(new MethodScheme("print", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                System.out.println(parameters[0].getValue());
                return null;
            }
        }));
        // Static method: exit
        os.registerMethod(new MethodScheme("exit", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... args) {
                System.exit(-1);
                return null;
            }
        }));
        // Static method: currentTimeMillis
        os.registerMethod(new MethodScheme("currentTimeMillis", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... args) {
                return new PNumber(System.currentTimeMillis());
            }
        }));
        // Static method: nanoTime
        os.registerMethod(new MethodScheme("nanoTime", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... args) {
                return new PNumber(System.nanoTime());
            }
        }));
    }

}

