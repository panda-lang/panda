package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.lang.BooleanEssence;

public class WhileBlock extends Block {

    public WhileBlock() {
        super.setName("while::" + atomicInteger.incrementAndGet());
    }

    @Override
    public Essence run(Particle particle) {
        Factor factor = factors[0];
        while (factor.<BooleanEssence>getValue(particle).isTrue()) {
            Essence o = super.run(particle);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(WhileBlock.class, "while").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new WhileBlock();
                current.setFactors(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

}
