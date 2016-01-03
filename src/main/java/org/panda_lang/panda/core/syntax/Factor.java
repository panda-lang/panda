package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;
import org.panda_lang.panda.util.VariableMap;

public class Factor implements NamedExecutable {

    enum Type {
        DEFINED,
        VARIABLE,
        RUNTIME;
    }

    private final Type type;
    private String dataType;
    private Essence object;
    private String variable;
    private VariableMap map;
    private Runtime runtime;
    private Essence value;

    public Factor(String type, Essence object) {
        this.type = Type.DEFINED;
        this.object = object;
        this.dataType = type;
    }

    public Factor(String type, VariableMap map, String variable) {
        this.type = Type.VARIABLE;
        this.dataType = type;
        this.map = map;
        this.variable = variable;
    }

    public Factor(String type, VariableMap map, Runtime runtime) {
        this.type = Type.RUNTIME;
        this.dataType = type;
        this.map = map;
        this.runtime = runtime;
    }

    @Override
    public Essence run(Particle particle) {
        return getValue();
    }

    public void setValue(Essence o) {
        switch (type) {
            case DEFINED:
                value = o;
                break;
            case VARIABLE:
                map.put(variable, o);
                value = o;
                break;
            default:
                value = o;
                break;
        }
        if (value != null) {
            dataType = value.getType();
        }
    }

    public void setDataType(String type) {
        this.dataType = type;
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

    public String getDataType() {
        return dataType;
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
                value = map.get(variable);
                break;
            case RUNTIME:
                if (runtime == null) {
                    System.out.println("Runtime is null. Factor info: " + this);
                    return null;
                }
                value = runtime.run(new Particle());
                break;
            default:
                System.out.println("Factor type is not defined. Factor info: " + this);
                break;
        }
        this.value = value;
        if (dataType == null && value != null) {
            dataType = value.getType();
        }
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Factor{");
        sb.append("type=").append(type);
        sb.append(", dataType='").append(dataType).append('\'');
        sb.append(", object=").append(object);
        sb.append(", variable='").append(variable).append('\'');
        sb.append(", map=").append(map);
        sb.append(", runtime=").append(runtime);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

}
