package org.pandalang.panda.core.syntax.block;

import org.pandalang.panda.core.ElementsBucket;
import org.pandalang.panda.core.parser.improved.Atom;
import org.pandalang.panda.core.parser.improved.essential.ParameterParser;
import org.pandalang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.pandalang.panda.core.scheme.BlockScheme;
import org.pandalang.panda.core.syntax.Block;

public class ClassBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ClassBlock.class, false, "class").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ClassBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

}
