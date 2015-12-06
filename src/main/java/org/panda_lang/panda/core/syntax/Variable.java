package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Variable implements NamedExecutable {

    private final Block block;
    private final String variable;
    private final Parameter parameter;

    public Variable(Block block, String variable, Parameter parameter) {
        this.block = block;
        this.variable = variable;
        this.parameter = parameter;
    }

    @Override
    public Essence run(Particle particle) {
        Essence object = parameter.getValue();
        block.setVariable(variable, object);
        return object;
    }

    @Override
    public String getName() {
        return this.variable;
    }

}
