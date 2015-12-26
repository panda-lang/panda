package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsPuller;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

    static {
        ElementsPuller.registerBlock(new BlockLayout(ElseThenBlock.class, false, "else").parser(new BlockInitializer() {
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
