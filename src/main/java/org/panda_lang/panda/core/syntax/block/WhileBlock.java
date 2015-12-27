package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.BlockCenter;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.lang.PBoolean;

public class WhileBlock extends Block {

    static {
        BlockCenter.registerBlock(new BlockLayout(WhileBlock.class, "while").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new WhileBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    public WhileBlock() {
        super.setName("while::" + System.nanoTime());
    }

    @Override
    public Essence run(Particle particle) {
        while (parameters[0].getValue(PBoolean.class).isTrue()) {
            Essence o = super.run(particle);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

}
