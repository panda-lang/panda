package org.panda_lang.panda;

import org.panda_lang.panda.core.syntax.Block;
import org.panda_lang.panda.core.syntax.Essence;
import org.panda_lang.panda.core.syntax.Factor;
import org.panda_lang.panda.core.syntax.block.PandaBlock;

import java.util.ArrayList;
import java.util.Collection;

public class PandaScript {

    private final Collection<PandaBlock> blocks;

    public PandaScript() {
        this.blocks = new ArrayList<>();
    }

    public void addPandaBlock(PandaBlock block) {
        this.blocks.add(block);
    }

    public Essence call(Class<? extends Block> blockType, String name, Factor... factors) {
        for (PandaBlock pandaBlock : blocks) {
            Essence essence = pandaBlock.call(blockType, name, factors);
            if (essence != null) {
                return essence;
            }
        }
        return null;
    }

}
