package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PObject;

public class Variable implements Executable {

    private final Block block;
    private final String variable;
    private final Parameter parameter;

    public Variable(Block block, String variable, Parameter parameter){
        this.block = block;
        this.variable = variable;
        this.parameter = parameter;
    }

    @Override
    public PObject run(Parameter... args) {
        PObject object = parameter.getValue();
        block.setVariable(variable, object);
        return object;
    }

    @Override
    public String getName(){
        return this.variable;
    }

}
