package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.scheme.BlockScheme;
import org.pandalang.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ElseThenBlock.class, false, "else").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ElseThenBlock();
                if (atom.getPrevious() instanceof IfThenBlock) {
                    ((IfThenBlock) atom.getPrevious()).setElseThenBlock(current);
                }
                return current;
            }
        }));
    }

    public ElseThenBlock() {
        super.setName("else-then::" + System.nanoTime());
    }

}
