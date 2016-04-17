package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;

public class Return implements Executable {

    private final Block block;
    private Factor factor;

    public Return(Block block, Factor factor) {
        this(block);
        this.factor = factor;
    }

    public Return(Block block) {
        this.block = block;
    }

    @Override
    public Essence execute(Alice alice) {
        return factor != null ? factor.getValue(alice) : null;
    }

    public final Block getBlock() {
        return block;
    }

}
