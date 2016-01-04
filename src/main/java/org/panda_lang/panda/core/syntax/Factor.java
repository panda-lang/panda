package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

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

    public Factor(Essence object) {
        this.type = Type.DEFINED;
        this.object = object;
    }

    public Factor(String variable) {
        this.type = Type.VARIABLE;
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

    public <T> T getValue(Class<T> clazz) {
        Essence object = getValue();
        if (object == null) {
            System.out.println("Cannot cast to " + clazz.getClass().getSimpleName() + "! Object == null");
            return null;
        }
        if (clazz.isInstance(object)) {
            return (T) object;
        }
        System.out.println("Cannot cast " + object.getClass().getSimpleName() + " to " + clazz.getSimpleName());
        return null;
    }

    public Essence getValue() {
        Essence value = null;
        switch (type) {
            case DEFINED:
                value = object;
                break;
            case VARIABLE:
                //value = map.get(variable);
                break;
            case RUNTIME:
                if (runtime == null) {
                    System.out.println("Runtime is null. Factor info: " + this);
                    return null;
                }
                value = runtime.run(new Particle());
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Factor{");
        sb.append("type=").append(type);
        sb.append(", object=").append(object);
        sb.append(", variable='").append(variable).append('\'');
        sb.append(", runtime=").append(runtime);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

}