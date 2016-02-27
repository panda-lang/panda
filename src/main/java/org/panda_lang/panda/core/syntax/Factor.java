package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

public class Factor implements NamedExecutable {

    enum Type {
        DEFINED,
        VARIABLE,
        RUNTIME
    }

    private final Type type;
    private Essence object;
    private String variable;
    private Runtime runtime;
    private Essence value;
    private Particle particle;

    public Factor(Essence object) {
        this.type = Type.DEFINED;
        this.object = object;
    }

    public Factor(Runtime runtime) {
        this.type = Type.RUNTIME;
        this.runtime = runtime;
    }

    public Factor(String variable) {
        this.type = Type.VARIABLE;
        this.variable = variable;
    }

    public Factor(Particle particle, String variable) {
        this.type = Type.VARIABLE;
        this.particle = particle;
        this.variable = variable;
    }

    @Override
    public Essence run(Particle particle) {
        return getValue(particle);
    }

    public <T extends Essence> T getValue() {
        return getValue(particle);
    }

    @SuppressWarnings("unchecked")
    public <T extends Essence> T getValue(Particle particle) {
        switch (type) {
            case DEFINED:
                this.value = object;
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
        return particle.getMemory().get(variable);
    }

    public Essence getRuntimeValue(Particle particle) {
        return runtime.run(particle);
    }

    public Essence getObject() {
        return object;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public String getName() {
        return toString();
    }

}