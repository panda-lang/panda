package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.NamedExecutable;
import org.panda_lang.panda.lang.PThread;

public class ThreadBlock extends Block {

    static {
        BlockCenter.registerBlock(new BlockLayout(ThreadBlock.class, "thread").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ThreadBlock();
                Factor[] factors = new FactorParser().parse(atom, atom.getBlockInfo().getParameters());
                current.setFactors(factors);
                return current;
            }
        }));
    }

    private PThread pThread;

    public ThreadBlock() {
        super.setName("thread::" + atomicInteger.incrementAndGet());
    }

    public Essence start(final Particle particle) {
        final Block block = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (NamedExecutable executable : block.getExecutables()) {
                    executable.run(particle);
                }
            }
        });
        if (factors != null && factors.length > 0) {
            Essence value = factors[0].getValue(particle);
            thread.setName(pThread.getName());
        }
        thread.start();
        return null;
    }

    @Override
    public Essence run(final Particle particle) {
        if (factors.length == 0) {
            start(particle);
            return null;
        }
        else {
            pThread = factors[0].getValue(particle);
            pThread.setBlock(this);
            pThread.setMemory(particle.getMemory());
            return pThread;
        }
    }

}
