package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Field implements NamedExecutable {

    private final String fieldName;

    public Field(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Essence run(Particle particle) {
        return null;
    }

    public Variable toVariable(Block block, Factor factor) {
        return new Variable(block, fieldName, factor);
    }

    @Override
    public String getName() {
        return fieldName;
    }

}
