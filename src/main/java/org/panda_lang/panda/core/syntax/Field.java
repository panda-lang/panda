package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Field implements NamedExecutable {

    private final String fieldName;
    private String dataType;
    private Factor factor;
    private Essence value;

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
        this.value = factor != null ? factor.getValue() : null;
        particle.getMemory().put(fieldName, value);
        return value;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getName() {
        return fieldName;
    }

}
