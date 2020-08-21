package org.panda_lang.language.architecture.type;

import java.util.Arrays;

public final class Signature {

    public enum Relation {
        DIRECT,
        EXTENDS,
        SUPER
    }

    private final Type type;
    private final Signature[] generics;
    private final Relation relation;

    public Signature(Type type, Signature[] generics, Relation relation) {
        this.type = type;
        this.generics = generics;
        this.relation = relation;
    }

    @Override
    public boolean equals(Object to) {
        if (this == to) {
            return true;
        }

        if (to == null || getClass() != to.getClass()) {
            return false;
        }

        Signature signature = (Signature) to;

        if (!type.equals(signature.type)) {
            return false;
        }

        if (!Arrays.equals(generics, signature.generics)) {
            return false;
        }

        return relation == signature.relation;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + Arrays.hashCode(generics);
        result = 31 * result + relation.hashCode();
        return result;
    }

}
