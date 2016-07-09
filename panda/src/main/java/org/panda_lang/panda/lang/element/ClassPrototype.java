package org.panda_lang.panda.lang.element;

public class ClassPrototype {

    private final String className;
    private final Field[] fields;
    private final Method[] methods;

    public ClassPrototype(ClassPrototypeBuilder classPrototypeBuilder) {
        this.className = classPrototypeBuilder.className;
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

    public static ClassPrototypeBuilder builder() {
        return new ClassPrototypeBuilder();
    }

    public static class ClassPrototypeBuilder {

        private String className;

        public ClassPrototype build() {
            return new ClassPrototype(this);
        }

    }

}
