package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.lang.PNumber;

public class ForBlock extends Block {

    static {
        ElementsBucket.registerBlock(new BlockScheme(ForBlock.class, "for", "loop").parser(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ForBlock();
                current.setParameters(new ParameterParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        }));
    }

    public ForBlock() {
        super.setName("for::" + System.nanoTime());
    }

    @Override
    public Essence run(Particle particle) {
        PNumber times = parameters[0].getValue().cast(PNumber.class);
        for (int i = 0; i < times.getNumber().intValue(); i++) {
            Essence o = super.run(particle);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

}
