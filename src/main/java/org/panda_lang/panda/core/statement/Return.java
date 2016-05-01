package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;

public class Return implements Executable {

    private final Block block;
    private RuntimeValue runtimeValue;

    public Return(Block block, RuntimeValue runtimeValue) {
        this(block);
        this.runtimeValue = runtimeValue;
    }

    public Return(Block block) {
        this.block = block;
    }

    @Override
    public Inst execute(Alice alice) {
        return runtimeValue != null ? runtimeValue.getValue(alice) : null;
    }

    public final Block getBlock() {
        return block;
    }

}
