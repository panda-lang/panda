package org.panda_lang.language.architecture.type;

public final class TypeContext {

    private final Type type;
    private final TypeScope scope;

    public TypeContext(Type type, TypeScope scope) {
        this.type = type;
        this.scope = scope;
    }

    public TypeScope getScope() {
        return scope;
    }

    public Type getType() {
        return type;
    }

}
