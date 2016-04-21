package org.panda_lang.panda.core.statement.association;

import org.panda_lang.panda.core.statement.Method;

import java.util.HashMap;
import java.util.Map;

public class Association<T> {

    private final Class<T> associatedClass;
    private final String associationName;
    private final Map<String, Method> methods;
    private ConstructorAssociation<T> constructor;

    public Association(Class<T> associatedClass, String associationName) {
        this.associatedClass = associatedClass;
        this.associationName = associationName;
        this.methods = new HashMap<>();
    }

    public void constructor(ConstructorAssociation<T> constructor) {
        this.constructor = constructor;
    }

    public void method(Method method) {
        this.methods.put(method.getName(), method);
    }

    public ConstructorAssociation<T> getConstructor() {
        return constructor;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public String getAssociationName() {
        return associationName;
    }

    public Class<T> getAssociatedClass() {
        return associatedClass;
    }

}
