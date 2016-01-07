package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.core.memory.Memory;

public class Factor implements NamedExecutable {

    enum Type {
        DEFINED,
        VARIABLE,
        RUNTIME;
    }

    private final Type type;
    private Essence object;
    private String variable;
    private Runtime runtime;
    private Essence value;
    private Memory memory;

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

    public Factor(Memory memory, String variable) {
        this.type = Type.VARIABLE;
        this.memory = memory;
        this.variable = variable;
    }

    @Override
    public Essence run(Particle particle) {
        return getValue(particle);
    }

    public Essence getSoftValue() {
        return value != null ? value : getValue();
    }

    public Essence getValue(Particle particle) {
        return getValue(memory);
    }

    public <T> T getValue(Class<T> clazz) {
        return (T) getValue();
    }

    public Essence getValue() {
        switch (type) {
            case DEFINED:
                this.value = object;
                break;
            case VARIABLE:
                this.value = memory.get(variable);
                break;
            case RUNTIME:
                this.value = runtime.run(new Particle());
                break;
        }
        return this.value;
    }

    public Essence getValue(Memory memory) {
        switch (type) {
            case DEFINED:
                this.value = object;
                break;
            case VARIABLE:
                this.value = memory.get(variable);
                break;
            case RUNTIME:
                this.value = runtime.run(new Particle(memory));
                break;
        }
        return this.value;
    }

    public Type getType() {
        return type;
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