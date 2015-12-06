package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.scheme.ConstructorScheme;
import org.panda_lang.panda.core.scheme.MethodScheme;
import org.panda_lang.panda.core.scheme.ObjectScheme;
import org.panda_lang.panda.core.syntax.Constructor;
import org.panda_lang.panda.core.syntax.Executable;
import org.panda_lang.panda.core.syntax.Parameter;
import org.panda_lang.panda.core.syntax.block.RunnableBlock;

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
        os.registerMethod(new MethodScheme("run", new Executable() {
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
