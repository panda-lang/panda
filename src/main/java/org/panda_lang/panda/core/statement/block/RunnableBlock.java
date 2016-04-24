package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.Block;

public class RunnableBlock extends Block {

    public RunnableBlock() {
        super.setName("runnable::" + blockIDAssigner.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(RunnableBlock.class, "runnable").initializer(new BlockInitializer() {
            @Override
            public Block initialize(ParserInfo atom) {
                Block current = new RunnableBlock();
                current.setRuntimeValues(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    @Override
    public Essence execute(Alice alice) {
        return super.execute(alice);
    }

}
