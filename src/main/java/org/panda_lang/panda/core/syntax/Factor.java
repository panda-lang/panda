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

    public Factor(String variable) {
        this.type = Type.VARIABLE;
        this.memory = memory;
        this.variable = variable;
    }

    public Factor(Runtime runtime) {
        this.type = Type.RUNTIME;
        this.runtime = runtime;
    }

    @Override
    public Essence run(Particle particle) {
        return getValue();
    }

    public Essence getSoftValue() {
        return value != null ? value : getValue();
    }

    public <T> T getValue(Class<T> clazz) {
        return (T) getValue();
    }

    public Essence getValue() {
        Essence value = null;
        switch (type) {
            case DEFINED:
                value = object;
                break;
            case VARIABLE:
                //value = GlobalVariables.VARIABLES.get(variable);
                break;
            case RUNTIME:
                value = runtime.run(new Particle());
                break;
        }
        this.value = value;
        return this.value;
    }

    public Essence getValue(Memory memory) {
        Essence value = null;
        switch (type) {
            case DEFINED:
                value = object;
                break;
            case VARIABLE:
                value = memory.get(variable);
                break;
            case RUNTIME:
                value = runtime.run(new Particle(memory));
                break;
        }
        this.value = value;
        return this.value;
    }

    public Type getType() {
        return type;
    }

    public Essence getObject() {
        return object;
    }

    public Runtime getRuntime() {
        return runtime;
    }

    public String getVariable() {
        return variable;
    }

    @Override
    public String getName() {
        return toString();
    }

}