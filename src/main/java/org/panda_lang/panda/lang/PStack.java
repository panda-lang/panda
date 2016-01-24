package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.*;

import java.util.Stack;

public class PStack extends PObject {

    static {
        Vial vial = new Vial("Stack");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new PStack();
            }
        });
        vial.method(new Method("push", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return particle.<PStack>getValueOfInstance().getStack().push(particle.getValueOfFactor(0));
            }
        }));
        vial.method(new Method("peek", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return particle.<PStack>getValueOfInstance().getStack().peek();
            }
        }));
        vial.method(new Method("pop", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return particle.<PStack>getValueOfInstance().getStack().pop();
            }
        }));
    }

    private final Stack<Essence> stack;

    public PStack() {
        this.stack = new Stack<>();
    }

    public Stack<Essence> getStack() {
        return stack;
    }

    @Override
    public Object getJavaValue() {
        return stack;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (Essence essence : stack) {
            if (node.length() != 0) {
                node.append(", ");
            }
            node.append(essence);
        }
        return node.toString();
    }

}
