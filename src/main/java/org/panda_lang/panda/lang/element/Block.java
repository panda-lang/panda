package org.panda_lang.panda.lang.element;

import org.panda_lang.panda.core.work.Executable;
import org.panda_lang.panda.core.work.Wrapper;

public class Block extends Wrapper {

    protected Executable blockExecutable;

    public Block() {
    }

    public Block(Executable blockExecutable) {
        this.blockExecutable = blockExecutable;
    }

    @Override
    public void execute() {
        blockExecutable.execute();
    }

    public Executable getBlockExecutable() {
        return blockExecutable;
    }

}
