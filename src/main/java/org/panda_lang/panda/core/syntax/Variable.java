package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Variable implements NamedExecutable {

    private final Block block;
    private final String variable;
    private final Factor factor;

    public Variable(Block block, String variable, Factor factor) {
        this.block = block;
        this.variable = variable;
        this.factor = factor;
    }

    @Override
    public Essence run(Particle particle) {
        Essence object = factor.getValue();
        block.setVariable(variable, object);
        return object;
    }

    @Override
    public String getName() {
        return this.variable;
    }

}
