package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

    public ElseThenBlock() {
        super.setName("else-then::" + atomicInteger.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(ElseThenBlock.class, false, "else").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ElseThenBlock();
                if (atom.getPrevious() instanceof IfThenBlock) {
                    ((IfThenBlock) atom.getPrevious()).setElseThenBlock(current);
                }
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

}
