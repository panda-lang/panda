package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.lang.PNull;
import org.panda_lang.panda.lang.PObject;

public class Parameter implements Executable {

    enum Type {
        DEFINED,
        VARIABLE,
        RUNTIME;
    }

    private final Type type;
    private PObject object;
    private String variable;
    private Block block;
    private String dataType;
    private Runtime runtime;
    private PObject value;

    public Parameter(String type, PObject object) {
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
    public PObject run(Parameter... parameters) {
        return getValue();
    }

    public void setValue(PObject o) {
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

    public PObject getObject() {
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
        PObject object = getValue();
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

    public PObject getValue() {
        PObject value = new PNull();
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
