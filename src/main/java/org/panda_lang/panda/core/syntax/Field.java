package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Field implements NamedExecutable {

    private final String fieldName;
    private String dataType;
    private Factor factor;

    public Field(String fieldName) {
        this.fieldName = fieldName;
    }

    public Field(String fieldName, Factor factor) {
        this.fieldName = fieldName;
        this.factor = factor;
    }

    public Field(String dataType, String fieldName, Factor factor) {
        this.fieldName = fieldName;
        this.dataType = dataType;
        this.factor = factor;
    }

    @Override
    public Essence run(Particle particle) {
        return factor != null ? factor.getValue() : null;
    }

    public Variable toVariable(Block block, Factor factor) {
        return new Variable(block, fieldName, factor);
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getName() {
        return fieldName;
    }

}
