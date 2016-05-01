package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;

import java.util.Stack;

public class StackInst extends ObjectInst {

    static {
        Structure structure = new Structure("Stack");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return new StackInst();
            }
        });
        structure.method(new Method("push", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return alice.<StackInst> getValueOfInstance().getStack().push(alice.getValueOfFactor(0));
            }
        }));
        structure.method(new Method("peek", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return alice.<StackInst> getValueOfInstance().getStack().peek();
            }
        }));
        structure.method(new Method("pop", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return alice.<StackInst> getValueOfInstance().getStack().pop();
            }
        }));
    }

    private final Stack<Inst> stack;

    public StackInst() {
        this.stack = new Stack<>();
    }

    public Stack<Inst> getStack() {
        return stack;
    }

    @Override
    public Object getJavaValue() {
        return stack;
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (Inst inst : stack) {
            if (node.length() != 0) {
                node.append(", ");
            }
            node.append(inst);
        }
        return node.toString();
    }

}
