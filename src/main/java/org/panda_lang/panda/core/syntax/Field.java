package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

import java.util.concurrent.atomic.AtomicInteger;

public class Field implements NamedExecutable {

    protected static final AtomicInteger atomicInteger = new AtomicInteger();

    private final int id;
    private final String fieldName;
    private String dataType;
    private Factor factor;
    private Essence value;

    public Field(String fieldName) {
        this.id = atomicInteger.incrementAndGet();
        this.fieldName = fieldName;
    }

    public Field(String fieldName, Factor factor) {
        this(fieldName);
        this.factor = factor;
    }

    public Field(String dataType, String fieldName, Factor factor) {
        this(fieldName);
        this.dataType = dataType;
        this.factor = factor;
    }

    @Override
    public Essence run(Particle particle) {
        value = factor != null ? factor.getValue(particle) : null;
        particle.getMemory().put(fieldName, value);
        return value;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Essence getValue() {
        return value;
    }

    public Factor getFactor() {
        return factor;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    public int getID() {
        return id;
    }

}
