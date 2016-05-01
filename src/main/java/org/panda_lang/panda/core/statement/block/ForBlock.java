package org.panda_lang.panda.core.statement.block;

import org.panda_lang.panda.Panda;
import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.parser.ParserInfo;
import org.panda_lang.panda.core.parser.essential.FactorParser;
import org.panda_lang.panda.core.parser.essential.util.BlockInitializer;
import org.panda_lang.panda.core.parser.essential.util.BlockLayout;
import org.panda_lang.panda.core.parser.essential.util.Numeric;
import org.panda_lang.panda.core.statement.Block;

public class ForBlock extends Block {

    public ForBlock() {
        super.setName("for::" + blockIDAssigner.incrementAndGet());
    }

    public static void initialize(Panda panda) {
        BlockLayout blockLayout = new BlockLayout(ForBlock.class, "for").initializer(new BlockInitializer() {
            @Override
            public Block initialize(ParserInfo atom) {
                Block current = new ForBlock();
                current.setRuntimeValues(new FactorParser().parse(atom, atom.getBlockInfo().getParameters()));
                return current;
            }
        });
        panda.getPandaCore().registerBlock(blockLayout);
    }

    @Override
    public Inst execute(Alice alice) {
        Numeric times = runtimeValues[0].getValue(alice);
        for (int i = 0; i < times.getInt(); i++) {
            Inst o = super.execute(alice);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

}
