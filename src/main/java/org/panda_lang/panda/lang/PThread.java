package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.syntax.block.ThreadBlock;

public class PThread extends PObject {

    static {
        /*
        // Register object
        ObjectScheme os = new ObjectScheme(PThread.class, "Thread");
        // Constructor
        os.registerConstructor(new ConstructorScheme(new Constructor<PThread>() {
            @Override
            public PThread run(Parameter... parameters) {
                return parameters != null && parameters.length != 0 ? new PThread(parameters[0].getValue().toString()) : new PThread();
            }
        }));
        // Method: start
        os.registerMethod(new MethodScheme("start", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PThread thread = instance.getValue(PThread.class);
                thread.start(parameters);
                return null;
            }
        }));
        // Method: getName
        os.registerMethod(new MethodScheme("getName", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                PThread pThread = instance.getValue(PThread.class);
                return new PString(pThread.getName());
            }
        }));
        // Static method: currentThread
        os.registerMethod(new MethodScheme("currentThread", new Executable() {
            @Override
            public PObject run(Parameter instance, Parameter... parameters) {
                return new PThread(Thread.currentThread());
            }
        }));
        */
    }

    private String name;
    private ThreadBlock block;
    private Thread thread;

    public PThread() {
    }

    public PThread(String name) {
        this.name = name;
    }

    public PThread(Thread thread) {
        this.thread = thread;
        this.name = thread.getName();
    }

    public void start(Particle particle) {
        if (this.block != null) {
            block.start(particle);
        }
    }

    public void setBlock(ThreadBlock block) {
        this.block = block;
    }

    public String getName() {
        return name != null ? name : "ThreadBlock";
    }

    public Thread getThread() {
        return thread;
    }

    @Override
    public String getType() {
        return "Thread";
    }

}
