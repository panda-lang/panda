package org.panda_lang.panda.implementation.element.field;

public class Field {

    private final int wrapperID;
    private final int fieldID;
    private final String fieldType;
    private final String fieldName;

    public Field(int wrapperID, int fieldID, String fieldType, String fieldName) {
        this.wrapperID = wrapperID;
        this.fieldID = fieldID;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public int getFieldID() {
        return fieldID;
    }

    public int getWrapperID() {
        return wrapperID;
    }

}
