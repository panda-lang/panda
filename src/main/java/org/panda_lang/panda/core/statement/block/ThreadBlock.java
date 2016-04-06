package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.Executable;
import org.panda_lang.panda.core.statement.Factor;
import org.panda_lang.panda.lang.ThreadEssence;

public class ThreadBlock extends Block {

    private ThreadEssence pThread;

    public ThreadBlock() {
        super.setName("thread::" + atomicInteger.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(ThreadBlock.class, "thread").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ThreadBlock();
                Factor[] factors = new FactorParser().parse(atom, atom.getBlockInfo().getParameters());
                current.setFactors(factors);
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    public Essence start(final Alice alice) {
        final Block block = this;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (Executable executable : block.getExecutables()) {
                    executable.run(alice);
                }
            }
        });
        if (factors != null && factors.length > 0) {
            Essence value = factors[0].getValue(alice);
            thread.setName(pThread.getName());
        }
        thread.start();
        return null;
    }

    @Override
    public Essence run(final Alice alice) {
        if (factors.length == 0) {
            start(alice);
            return null;
        }
        else {
            pThread = factors[0].getValue(alice);
            pThread.setBlock(this);
            pThread.setMemory(alice.getMemory());
            return pThread;
        }
    }

}
