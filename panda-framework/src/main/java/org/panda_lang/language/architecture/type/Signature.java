package org.panda_lang.language.architecture.type;

import org.panda_lang.utilities.commons.text.ContentJoiner;

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

        if (!type.equals(signature.getType())) {
            return false;
        }

        if (!Arrays.equals(generics, signature.getGenerics())) {
            return false;
        }

        return relation == signature.getRelation();
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + Arrays.hashCode(generics);
        result = 31 * result + relation.hashCode();
        return result;
    }

    public boolean isAssignableFrom(Signature signature) {
        if (generics.length != signature.getGenerics().length) {
            return false;
        }

        if (Relation.DIRECT == relation && !type.equals(signature.getType())) {
            return false;
        }

        if (Relation.EXTENDS == relation && !type.isAssignableFrom(signature.getType())) {
            return false;
        }

        if (Relation.SUPER == relation && signature.getType().isAssignableFrom(type)) {
            return false;
        }

        for (int index = 0; index < generics.length; index++) {
            Signature generic = generics[index];
            Signature toGeneric = signature.getGenerics()[index];

            if (generic.isAssignableFrom(toGeneric)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return getType().getSimpleName() + "<" + ContentJoiner.on(" & ").join(generics) + ">";
    }

    public Relation getRelation() {
        return relation;
    }

    public Signature[] getGenerics() {
        return generics;
    }

    public Type getType() {
        return type;
    }

}
