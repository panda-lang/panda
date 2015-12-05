package org.pandalang.panda.lang;

import org.pandalang.panda.core.scheme.ConstructorScheme;
import org.pandalang.panda.core.scheme.MethodScheme;
import org.pandalang.panda.core.scheme.ObjectScheme;
import org.pandalang.panda.core.syntax.Constructor;
import org.pandalang.panda.core.syntax.IExecutable;
import org.pandalang.panda.core.syntax.Parameter;
import org.pandalang.panda.core.syntax.block.RunnableBlock;

public class PRunnable extends PObject {

    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PRunnable.class, "Runnable");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PRunnable>() {
            @Override
            public PRunnable run(Parameter... parameters) {
                return new PRunnable();
            }
        }));
        // Method: run
        os.registerMethod(new MethodScheme("run", new IExecutable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                return instance.getValue(PRunnable.class).run(parameters);
            }
        }));
    }

    private RunnableBlock block;

    public PObject run(Parameter... vars) {
        return block != null ? block.run(vars) : null;
    }

    public void setBlock(RunnableBlock block) {
        this.block = block;
    }

    @Override
    public String getType() {
        return "Runnable";
    }

}
