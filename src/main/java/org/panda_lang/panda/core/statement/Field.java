package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

import java.util.concurrent.atomic.AtomicInteger;

public class Field implements NamedExecutable {

    protected static final AtomicInteger identifier = new AtomicInteger();

    private final int id;
    private final String fieldName;
    private String dataType;
    private RuntimeValue runtimeValue;
    private Essence value;

    public Field(String fieldName) {
        this.id = identifier.incrementAndGet();
        this.fieldName = fieldName;
    }

    public Field(String fieldName, RuntimeValue runtimeValue) {
        this(fieldName);
        this.runtimeValue = runtimeValue;
    }

    public Field(String dataType, String fieldName, RuntimeValue runtimeValue) {
        this(fieldName);
        this.dataType = dataType;
        this.runtimeValue = runtimeValue;
    }

    public static AtomicInteger getIdentifier() {
        return identifier;
    }

    @Override
    public Essence execute(Alice alice) {
        value = runtimeValue != null ? runtimeValue.getValue(alice) : null;
        alice.getMemory().put(fieldName, value);
        return value;
    }

    public Essence getValue() {
        return value;
    }

    public RuntimeValue getRuntimeValue() {
        return runtimeValue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String getName() {
        return fieldName;
    }

    public int getID() {
        return id;
    }

}
