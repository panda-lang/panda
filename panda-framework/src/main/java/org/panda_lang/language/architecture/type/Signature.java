package org.panda_lang.language.architecture.type;

import org.panda_lang.utilities.commons.function.Result;
import org.panda_lang.utilities.commons.text.ContentJoiner;

import java.util.Arrays;

public final class Signature {

    public enum Relation {
        DIRECT,
        EXTENDS,
        SUPER
    }

    private final Result<Type, String> type;
    private final Signature[] generics;
    private final Relation relation;

    public Signature(Result<Type, String> type, Signature[] generics, Relation relation) {
        this.type = type;
        this.generics = generics;
        this.relation = relation;
    }

    public boolean isAbstract() {
        return type.isErr();
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

        if (Relation.EXTENDS == relation && !satisfies(signature.getType())) {
            return false;
        }

        if (Relation.SUPER == relation && signature.satisfies(type)) {
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

    private boolean satisfies(Result<Type, String> toType) {
        return false;
    }

    @Override
    public String toString() {
        return getType().map(Type::getSimpleName).getAny() + "<" + ContentJoiner.on(" & ").join(generics) + ">";
    }

    public Relation getRelation() {
        return relation;
    }

    public Signature[] getGenerics() {
        return generics;
    }

    public Result<Type, String> getType() {
        return type;
    }

}
