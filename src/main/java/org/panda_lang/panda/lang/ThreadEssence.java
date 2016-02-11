package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.block.ThreadBlock;

public class ThreadEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Thread");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return particle.hasFactors() ? new ThreadEssence(particle.getValueOfFactor(0).toString()) : new ThreadEssence();
            }
        });
        vial.method(new Method("start", new Executable() {
            @Override
            public Essence run(Particle particle) {
                ThreadEssence thread = particle.getValueOfInstance();
                thread.start(particle);
                return thread;
            }
        }));
        vial.method(new Method("getName", new Executable() {
            @Override
            public Essence run(Particle particle) {
                ThreadEssence thread = particle.getValueOfInstance();
                return new StringEssence(thread.getName());
            }
        }));
        vial.method(new Method("currentThread", new Executable() {
            @Override
            public Essence run(Particle particle) {
                return new ThreadEssence(Thread.currentThread());
            }
        }));
    }

    private String name;
    private ThreadBlock block;
    private Thread thread;
    private Memory memory;

    public ThreadEssence() {
    }

    public ThreadEssence(String name) {
        this();
        this.name = name;
    }

    public ThreadEssence(Thread thread) {
        this(thread.getName());
        this.thread = thread;
    }

    public void start(Particle particle) {
        if (memory != null) {
            Memory threadMemory = new Memory(memory);
            particle.setMemory(threadMemory);
        }
        if (this.block != null) {
            block.start(particle);
        }
        else if (thread != null) {
            thread.start();
        }
    }

    public void setBlock(ThreadBlock block) {
        this.block = block;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public String getName() {
        return name != null ? name : "ThreadBlock";
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
