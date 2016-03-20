package org.panda_lang.panda.core.syntax.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.lang.BooleanEssence;

public class IfThenBlock extends Block {

    private Block elseThenBlock;

    public IfThenBlock() {
        super.setName("if-then::" + atomicInteger.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(IfThenBlock.class, "if").initializer(new BlockInitializer() {
            @Override
            public Block initialize(Atom atom) {
                Block current = new IfThenBlock();
                current.setFactors(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    @Override
    public Essence run(Particle particle) {
        BooleanEssence flag = factors[0].getValue(particle);
        if (flag != null && flag.isTrue()) {
            return super.run(particle);
        }
        else if (elseThenBlock != null) {
            return elseThenBlock.run(particle);
        }
        return null;
    }

    public void setElseThenBlock(Block block) {
        this.elseThenBlock = block;
    }

}
