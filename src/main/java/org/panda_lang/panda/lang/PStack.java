package org.panda_lang.panda.lang;

import java.util.Stack;

public class PStack extends PObject {

    /*
    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PStack.class, "Stack");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PStack>() {
            @Override
            public PStack run(Factor... factors) {
                return new PStack();
            }
        }));
        // Method: push
        os.registerMethod(new MethodScheme("push", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PStack s = instance.getValue(PStack.class);
                return s.getStack().push(factors[0].getValue());
            }
        }));
        // Method: peek
        os.registerMethod(new MethodScheme("peek", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PStack s = instance.getValue(PStack.class);
                return s.getStack().peek();
            }
        }));
        // Method: pop
        os.registerMethod(new MethodScheme("pop", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                PStack s = instance.getValue(PStack.class);
                return s.getStack().pop();
            }
        }));
    }
    */

    private final Stack<PObject> stack;

    public PStack() {
        this.stack = new Stack<>();
    }

    public Stack<PObject> getStack() {
        return stack;
    }

    @Override
    public String getType() {
        return "Stack";
    }

    @Override
    public String toString() {
        StringBuilder node = new StringBuilder();
        for (PObject o : stack) {
            if (node.length() != 0) {
                node.append(", ");
            }
            node.append(o);
        }
        return node.toString();
    }

}
