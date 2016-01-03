package org.panda_lang.panda.lang;

public class PRunnable extends PObject {
/*
    static {
        // Register object
        ObjectScheme os = new ObjectScheme(PRunnable.class, "Runnable");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PRunnable>() {
            @Override
            public PRunnable run(Factor... factors) {
                return new PRunnable();
            }
        }));
        // Method: run
        os.registerMethod(new MethodScheme("run", new Executable() {
            @Override
            public PObject run(Factor instance, Factor... factors) {
                return instance.getValue(PRunnable.class).run(factors);
            }
        }));
    }

    private RunnableBlock block;

    public PObject run(Factor... vars) {
        return block != null ? block.run(vars) : null;
    }

    public void setBlock(RunnableBlock block) {
        this.block = block;
    }

    @Override
    public String getType() {
        return "Runnable";
    }
*/
}
