package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;

public class RunnableBlock extends Block {

    static {
        BlockCenter.registerBlock(new BlockLayout(RunnableBlock.class, "runnable").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new RunnableBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    public RunnableBlock() {
        super.setName("runnable::" + System.nanoTime());
    }

}
