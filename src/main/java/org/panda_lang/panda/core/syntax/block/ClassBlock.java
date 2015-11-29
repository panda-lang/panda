package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.parser.improved.Atom;
import org.panda_lang.panda.core.parser.improved.essential.ParameterParser;
import org.panda_lang.panda.core.parser.improved.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;

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
