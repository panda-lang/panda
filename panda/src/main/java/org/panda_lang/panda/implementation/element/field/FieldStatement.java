package org.panda_lang.panda.implementation.element.field;

import org.panda_lang.framework.structure.Statement;

public class FieldStatement implements Statement {

    private final Field field;
    private final int wrapperID;
    private final int fieldID;

    public FieldStatement(int wrapperID, int fieldID, Field field) {
        this.field = field;
        this.wrapperID = wrapperID;
        this.fieldID = fieldID;
    }

    public int getFieldID() {
        return fieldID;
    }

    public int getWrapperID() {
        return wrapperID;
    }

    public Field getField() {
        return field;
    }

}
