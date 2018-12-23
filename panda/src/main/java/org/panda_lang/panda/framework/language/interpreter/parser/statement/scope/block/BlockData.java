package org.panda_lang.panda.framework.language.interpreter.parser.statement.scope.block;

import org.panda_lang.panda.framework.design.architecture.dynamic.Block;

public class BlockData {

    private final Block block;
    private final boolean unlisted;

    public BlockData(Block block, boolean unlisted) {
        this.block = block;
        this.unlisted = unlisted;
    }

    public BlockData(Block block) {
        this(block, false);
    }

    public boolean isUnlisted() {
        return unlisted;
    }

    public Block getBlock() {
        return block;
    }

}
