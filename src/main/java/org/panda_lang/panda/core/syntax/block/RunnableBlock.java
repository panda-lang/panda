package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;

public class RunnableBlock extends Block {

    public RunnableBlock() {
        super.setName("runnable::" + atomicInteger.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(RunnableBlock.class, "runnable").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new RunnableBlock();
                current.setFactors(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    @Override
    public Essence run(Alice alice) {
        return super.run(alice);
    }

}
