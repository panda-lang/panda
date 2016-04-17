package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.Constructor;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Method;
import org.panda_lang.panda.core.statement.Vial;
import org.panda_lang.panda.core.statement.block.RunnableBlock;

public class RunnableEssence extends ObjectEssence {

    static {
        Vial vial = new Vial("Runnable");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence execute(Alice alice) {
                return new RunnableEssence();
            }
        });
        vial.method(new Method("execute", new Executable() {
            @Override
            public Essence execute(Alice alice) {
                RunnableEssence runnable = alice.getValueOfInstance();
                return runnable.execute(alice);
            }
        }));
    }

    private RunnableBlock block;
    private Memory memory;

    public RunnableEssence() {
    }

    public Essence execute(Alice alice) {
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
