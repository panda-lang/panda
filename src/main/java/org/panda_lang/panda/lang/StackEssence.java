package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.syntax.*;

import java.util.Stack;

public class StackEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Stack");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Alice alice) {
                return new StackEssence();
            }
        });
        vial.method(new Method("push", new Executable() {
            @Override
            public Essence run(Alice alice) {
                return alice.<StackEssence> getValueOfInstance().getStack().push(alice.getValueOfFactor(0));
            }
        }));
        vial.method(new Method("peek", new Executable() {
            @Override
            public Essence run(Alice alice) {
                return alice.<StackEssence> getValueOfInstance().getStack().peek();
            }
        }));
        vial.method(new Method("pop", new Executable() {
            @Override
            public Essence run(Alice alice) {
                return alice.<StackEssence> getValueOfInstance().getStack().pop();
            }
        }));
    }

    private final Stack<Essence> stack;

    public StackEssence() {
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
