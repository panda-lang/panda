package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Inst;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

import java.util.concurrent.atomic.AtomicInteger;

public class Field implements NamedExecutable {

    protected static final AtomicInteger identifier = new AtomicInteger();

    private final int id;
    private final String fieldName;
    private String dataType;
    private RuntimeValue runtimeValue;
    private Inst value;

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
    public Inst execute(Alice alice) {
        value = runtimeValue != null ? runtimeValue.getValue(alice) : null;
        alice.getMemory().put(fieldName, value);
        return value;
    }

    public Inst getValue() {
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
