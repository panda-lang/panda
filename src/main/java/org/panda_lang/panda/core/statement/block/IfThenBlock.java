package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.Atom;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.lang.BooleanEssence;

public class IfThenBlock extends Block {

    private Block elseThenBlock;

    public IfThenBlock() {
        super.setName("if-then::" + blockIDAssigner.incrementAndGet());
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
    public Essence execute(Alice alice) {
        BooleanEssence flag = factors[0].getValue(alice);
        if (flag != null && flag.isTrue()) {
            return super.execute(alice);
        }
        else if (elseThenBlock != null) {
            return elseThenBlock.execute(alice);
        }
        return null;
    }

    public void setElseThenBlock(Block block) {
        this.elseThenBlock = block;
    }

}
