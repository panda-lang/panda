package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.VialCenter;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.block.ThreadBlock;

public class PThread extends PObject {

    private static final Vial vial;

    static {
        vial = VialCenter.initializeVial("Thread");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return particle.hasParameters() ? new PThread(particle.getValue(0).toString()) : new PThread();
            }
        });
        vial.method(new Method("start", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PThread thread = particle.getInstance(PThread.class);
                thread.start(particle);
                return thread;
            }
        }));
        vial.method(new Method("getName", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PThread thread = particle.getInstance(PThread.class);
                return new PString(thread.getName());
            }
        }));
        vial.method(new Method("currentThread", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new PThread(Thread.currentThread());
            }
        }));
    }

    private String name;
    private ThreadBlock block;
    private Thread thread;

    public PThread() {
        super(vial);
    }

    public PThread(String name) {
        this();
        this.name = name;
    }

    public PThread(Thread thread) {
        this(thread.getName());
        this.thread = thread;
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

    @Override
    public Object getJavaValue() {
        return thread;
    }

    @Override
    public String toString() {
        return thread != null ? thread.getName() : name;
    }

}
