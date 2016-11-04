package org.panda_lang.panda.implementation.element.field;

public class Field {

    private final int fieldID;
    private final String fieldName;
    private final String fieldType;
    private final int typeID;
    private int valuePointer;

    public Field(int fieldID, String fieldName, String fieldType, int typeID) {
        this.fieldID = fieldID;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.typeID = typeID;
    }

    public void setValuePointer(int valuePointer) {
        this.valuePointer = valuePointer;
    }

    public int getValuePointer() {
        return valuePointer;
    }

    public int getTypeID() {
        return typeID;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public int getFieldID() {
        return fieldID;
    }

}
