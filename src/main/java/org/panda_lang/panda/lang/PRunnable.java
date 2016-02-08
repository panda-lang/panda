package org.panda_lang.panda.lang;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.*;
import org.panda_lang.panda.core.syntax.block.RunnableBlock;

public class PRunnable extends PObject {

    static {
        Vial vial = new Vial("Runnable");
        vial.group("panda.lang");
        vial.constructor(new Constructor() {
            @Override
            public Essence run(Particle particle) {
                return new PRunnable();
            }
        });
        vial.method(new Method("run", new Executable() {
            @Override
            public Essence run(Particle particle) {
                PRunnable runnable = particle.getValueOfInstance();
                return runnable.run(particle);
            }
        }));
    }

    private RunnableBlock block;
    private Memory memory;

    public PRunnable() {
    }

    public Essence run(Particle particle) {
        if (memory != null) {
            Memory threadMemory = new Memory(memory);
            particle.setMemory(threadMemory);
        }
        return block != null ? block.run(particle) : null;
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
