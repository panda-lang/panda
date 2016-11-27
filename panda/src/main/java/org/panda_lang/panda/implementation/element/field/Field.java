package org.panda_lang.panda.implementation.element.field;

public class Field {

    private final String name;
    private final String type;

    public Field(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
