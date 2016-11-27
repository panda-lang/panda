package org.panda_lang.panda.implementation.element.struct;

import org.panda_lang.panda.implementation.element.field.Field;
import org.panda_lang.panda.implementation.element.method.Method;

import java.util.HashMap;
import java.util.Map;

public class ClassPrototypeBuilder {

    private String className;
    private Map<String, Field> fields;
    private Map<String, Method> methods;

    public ClassPrototypeBuilder() {
        this.fields = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public ClassPrototypeBuilder className(String className) {
        this.className = className;
        return this;
    }

    public ClassPrototypeBuilder method(Method method) {
        methods.put(method.getMethodName(), method);
        return this;
    }

    public ClassPrototype build() {
        return new ClassPrototype(className, fields, methods);
    }

}
