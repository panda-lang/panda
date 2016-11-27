package org.panda_lang.panda.implementation.element.struct;

import org.panda_lang.panda.implementation.element.field.Field;
import org.panda_lang.panda.implementation.element.method.Method;

import java.util.HashMap;
import java.util.Map;

public class ClassPrototype {

    private final String className;
    private final Map<String, Field> fields;
    private final Map<String, Method> methods;

    public ClassPrototype(String className) {
        this(className, new HashMap<>(), new HashMap<>());
    }

    protected ClassPrototype(String className, Map<String, Field> fields, Map<String, Method> methods) {
        this.className = className;
        this.fields = fields;
        this.methods = methods;
    }

    public String getClassName() {
        return className;
    }

    public static ClassPrototypeBuilder builder() {
        return new ClassPrototypeBuilder();
    }

}
