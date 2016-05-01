package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.core.statement.block.ThreadBlock;

public class ThreadInst extends ObjectInst {

    static {
        Structure structure = new Structure("Thread");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return alice.hasFactors() ? new ThreadInst(alice.getValueOfFactor(0).toString()) : new ThreadInst();
            }
        });
        structure.method(new Method("start", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                ThreadInst thread = alice.getValueOfInstance();
                thread.start(alice);
                return thread;
            }
        }));
        structure.method(new Method("getName", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                ThreadInst thread = alice.getValueOfInstance();
                return new StringInst(thread.getName());
            }
        }));
        structure.method(new Method("currentThread", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                return new ThreadInst(Thread.currentThread());
            }
        }));
    }

    private String name;
    private ThreadBlock block;
    private Thread thread;
    private Memory memory;

    public ThreadInst() {
    }

    public ThreadInst(String name) {
        this();
        this.name = name;
    }

    public ThreadInst(Thread thread) {
        this(thread.getName());
        this.thread = thread;
    }

    public void start(Alice alice) {
        if (memory != null) {
            Memory threadMemory = new Memory(memory);
            alice.setMemory(threadMemory);
        }
        if (this.block != null) {
            block.start(alice);
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
