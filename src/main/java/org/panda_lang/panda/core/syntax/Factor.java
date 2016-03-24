package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

public class Factor implements NamedExecutable {

    private final Type type;
    private Essence definedEssence;
    private String variableName;
    private Runtime runtime;
    private Essence value;
    private String dataType;

    public Factor(Essence definedEssence) {
        this.type = Type.DEFINED;
        this.definedEssence = definedEssence;
        this.value = definedEssence;
    }

    public Factor(Runtime runtime) {
        this.type = Type.RUNTIME;
        this.runtime = runtime;
    }

    public Factor(String variableName) {
        this.type = Type.VARIABLE;
        this.variableName = variableName;
    }

    @Override
    public Essence run(Particle particle) {
        return getValue(particle);
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getCurrentValue() {
        return (T) this.value;
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValue(Particle particle) {
        switch (type) {
            case DEFINED:
                this.value = definedEssence;
                break;
            case VARIABLE:
                this.value = getMemoryValue(particle);
                break;
            case RUNTIME:
                if (particle == null) {
                    particle = new Particle();
                    particle.setMemory(new Memory());
                }
                this.value = getRuntimeValue(particle);
                break;
        }
        return (T) this.value;
    }

    public Essence getMemoryValue(Particle particle) {
        return particle.getMemory().get(variableName);
    }

    public Essence getRuntimeValue(Particle particle) {
        return runtime.run(particle);
    }

    public Essence getDefinedEssence() {
        return definedEssence;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public String getName() {
        return toString();
    }

    enum Type {
        DEFINED,
        VARIABLE,
        RUNTIME
    }

}