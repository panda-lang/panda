package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;

public class ForBlock extends Block {

    public ForBlock() {
        super.setName("for::" + atomicInteger.incrementAndGet());
    }

    @Override
    public Essence run(Particle particle) {
        Numeric times = factors[0].getValue();
        for (int i = 0; i < times.getInt(); i++) {
            Essence o = super.run(particle);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(ForBlock.class, "for", "loop").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new ForBlock();
                current.setFactors(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

}
