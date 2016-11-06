package org.panda_lang.panda.implementation.element.field;

public class Field {

    private final int fieldID;
    private final String fieldName;
    private final String fieldType;
    private final int typeID;

    public Field(int fieldID, String fieldName, String fieldType, int typeID) {
        this.fieldID = fieldID;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.typeID = typeID;
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
