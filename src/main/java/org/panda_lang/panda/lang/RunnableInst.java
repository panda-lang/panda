package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Structure;
import org.panda_lang.panda.core.statement.block.RunnableBlock;

public class RunnableInst extends ObjectInst {

    static {
        Structure structure = new Structure("Runnable");
        structure.group("panda.lang");
        structure.constructor(new Constructor() {
            @Override
            public Inst execute(Alice alice) {
                return new RunnableInst();
            }
        });
        structure.method(new Method("execute", new Executable() {
            @Override
            public Inst execute(Alice alice) {
                RunnableInst runnable = alice.getValueOfInstance();
                return runnable.execute(alice);
            }
        }));
    }

    private RunnableBlock block;
    private Memory memory;

    public RunnableInst() {
    }

    public Inst execute(Alice alice) {
        if (memory != null) {
            Memory threadMemory = new Memory(memory);
            alice.setMemory(threadMemory);
        }
        return block != null ? block.execute(alice) : null;
    }

    public void setBlock(RunnableBlock block) {
        this.block = block;
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    @Override
    public Object getJavaValue() {
        return block;
    }

}
