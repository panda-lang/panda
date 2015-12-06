package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Particle;

public class Parameter implements Executable {

    enum Type {
        DEFINED,
        VARIABLE,
        RUNTIME;
    }

    private final Type type;
    private Essence object;
    private String variable;
    private Block block;
    private String dataType;
    private Runtime runtime;
    private Essence value;

    public Parameter(String type, Essence object) {
        this.type = Type.DEFINED;
        this.object = object;
        this.dataType = type;
    }

    public Parameter(String type, Block block, String variable) {
        this.type = Type.VARIABLE;
        this.variable = variable;
        this.block = block;
        this.dataType = type;
    }

    public Parameter(String type, Block block, Runtime runtime) {
        this.type = Type.RUNTIME;
        this.dataType = type;
        this.runtime = runtime;
        this.block = block;
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
                block.setVariable(variable, o);
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

    public Block getBlock() {
        return block;
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
                value = block.getVariable(variable);
                break;
            case RUNTIME:
                if (runtime == null) {
                    System.out.println("Runtime is null. Parameter info: " + this.toString());
                    return null;
                }
                value = runtime.run();
                break;
            default:
                System.out.println("Parameter type is not defined. Parameter info: " + this.toString());
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
        return "@Parameter={" + type + "," + object + "," + variable + "," + block + "," + (block != null ? block.getName() : "null") + "," + dataType + "," + runtime + "," + value + "}";
    }

}
