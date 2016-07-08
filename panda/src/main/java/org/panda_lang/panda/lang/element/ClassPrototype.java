package org.panda_lang.panda.lang.element;

import java.util.HashMap;
import java.util.Map;

public class ClassPrototype {

    private final String className;
    private final Field[] fields;
    private final Method[] methods;

    public ClassPrototype(String className) {
        this.className = className;
        this.fields = null;
        this.methods = null;
    }

    public Method[] getMethods() {
        return methods;
    }

    public Field[] getFields() {
        return fields;
    }

    public String getClassName() {
        return className;
    }

}
