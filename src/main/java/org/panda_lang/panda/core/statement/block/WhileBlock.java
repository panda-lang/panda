package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.statement.Block;
import org.panda_lang.panda.core.statement.RuntimeValue;
import org.panda_lang.panda.lang.BooleanEssence;

public class WhileBlock extends Block {

    public WhileBlock() {
        super.setName("while::" + blockIDAssigner.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(WhileBlock.class, "while").initializer(new BlockInitializer() {
            @Override
            public Block initialize(ParserInfo atom) {
                Block current = new WhileBlock();
                current.setRuntimeValues(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    @Override
    public Essence execute(Alice alice) {
        RuntimeValue runtimeValue = runtimeValues[0];
        Essence essence = null;

        while (true) {
            BooleanEssence booleanEssence = runtimeValue.getValue(alice);
            if (booleanEssence == null || booleanEssence.isFalse() || essence != null) {
                break;
            }

            essence = super.execute(alice);
        }

        return essence;
    }

}
