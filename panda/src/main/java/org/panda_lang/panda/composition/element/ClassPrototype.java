package org.panda_lang.panda.composition.element;

public class ClassPrototype {

    protected String className;
    protected Field[] fields;
    protected Method[] methods;

    public ClassPrototype() {
    }

    public ClassPrototype(ClassPrototypeBuilder classPrototypeBuilder) {
        this.className = classPrototypeBuilder.className;
        this.fields = null;
        this.methods = null;
    }

    public Method[] getMethods() {
        return methods;
    }

    public void setMethods(Method[] methods) {
        this.methods = methods;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public static ClassPrototypeBuilder builder() {
        return new ClassPrototypeBuilder();
    }

    public static class ClassPrototypeBuilder {

        private String className;

        public ClassPrototypeBuilder className(String className) {
            this.className = className;
            return this;
        }

        public ClassPrototype build() {
            return new ClassPrototype(this);
        }

    }

}
