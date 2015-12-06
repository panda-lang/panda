package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.core.ElementsBucket;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.ParameterParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.scheme.BlockScheme;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;

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
        /*
        PObject object = parameters[0].getValue();
        if (object instanceof PNumber) {
            Number times = ((PNumber) object).getNumber();
            for (int i = 0; i < times.intValue(); i++) {
                PObject o = super.run(vars);
                if (o != null) {
                    return o;
                }
            }
        }
        */
        return null;
    }

}
