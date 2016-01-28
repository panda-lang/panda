package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Return implements NamedExecutable {

    private final Block block;
    private Factor factor;

    public Return(Block block, Factor factor) {
        this.block = block;
        this.factor = factor;
    }

    public Return(Block block) {
        this.block = block;
    }

    @Override
    public Essence run(Particle particle) {
        return factor.getValue(particle);
    }

    public final Block getBlock() {
        return block;
    }

    @Override
    public String getName() {
        return "return::" + block.getName();
    }

}
