package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.essential.CustomParser;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;

public class ElseThenBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ElseThenBlock.class, false, "else").parser(new CustomParser() {
            @Override
            public Block parse(Atom atom) {
                Block current = new ElseThenBlock();
                if (atom.getPrevious() instanceof IfThenBlock) {
                    ((IfThenBlock) atom.getPrevious()).setElseThenBlock(current);
                }
                return current;
            }
        }));
    }

    public ElseThenBlock() {
        super.setName("ElseThenBlock");
    }

}
