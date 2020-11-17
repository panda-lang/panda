package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

import java.util.Arrays;

abstract class AbstractSignature<V> implements Signature {

    private final Option<Signature> parent;
    private final V subject;
    private final Signature[] generics;
    private final Relation relation;

    protected AbstractSignature(@Nullable Signature parent, V subject, Signature[] generics, Relation relation) {
        this.parent = Option.of(parent);
        this.subject = subject;
        this.generics = generics;
        this.relation = relation;
    }

    @Override
    public boolean isGeneric() {
        return false;
    }

    @Override
    public GenericSignature toGeneric() {
        throw new UnsupportedOperationException("Signature " + toString() + " is not generic");
    }

    @Override
    public boolean isTyped() {
        return false;
    }

    @Override
    public TypedSignature toTyped() {
        throw new UnsupportedOperationException("Signature " + toString() + " is not typed");
    }

    @Override
    public Option<GenericSignature> findGeneric(String identifier) {
        for (Signature signature : generics) {
            if (signature.isGeneric()) {
                GenericSignature genericSignature = signature.toGeneric();

                if (identifier.equals(genericSignature.getLocalIdentifier())) {
                    return Option.of(genericSignature);
                }
            }
        }

        return parent.flatMap(parentSignature -> parentSignature.findGeneric(identifier));
    }

    @Override
    public boolean isAssignableFrom(Signature signature) {
        if (generics.length != signature.getGenerics().length) {
            return false;
        }

        if (Relation.DIRECT == relation && !subject.equals(signature)) {
            return false;
        }

        if (Relation.ANY == relation && !satisfies(signature)) {
            return false;
        }

        if (Relation.ALSO == relation && signature.satisfies(this)) {
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
    public Result<Signature, String> merge(Signature inheritor) {
        if (generics.length != inheritor.getGenerics().length) {
            return Result.error("Invalid amount of parameters in generic signature");
        }

        Option<Object> mergedSubject = resolveSubject(

        if (mergedSubject.isEmpty()) {
            return Result.error("Incompatible type");
        }

        if (Relation.DIRECT == relation && !subject.equals(inheritor.getSubject())) {
            return Result.error("Incompatible types");
        }

        if (Relation.ANY == relation && !satisfies(signature)) {
            return false;
        }

        if (Relation.ALSO == relation && signature.satisfies(this)) {
            return false;
        }

        for (int index = 0; index < generics.length; index++) {
            Signature generic = generics[index];
            Signature toGeneric = signature.getGenerics()[index];

            if (generic.isAssignableFrom(toGeneric)) {
                return false;
            }
        }

        return null;
    }

    private static Option<Object> resolveSubject(Signature root, Signature inheritor) {
        Object commonSubject = null;

        if (Relation.DIRECT == root.getRelation() && !root.getSubject().equals(inheritor.getSubject())) {
            if (root.isGeneric()) {
                GenericSignature genericRoot = root.toGeneric();

                if (inheritor.isGeneric()) {
                    GenericSignature genericInheritor = inheritor.toGeneric();
                    return Option.when(genericRoot.isAssignableFrom(genericInheritor), genericInheritor.getSubject());
                }

                if (inheritor.isTyped()) {
                    TypedSignature typedInheritor = inheritor.toTyped();
                    return Option.when(typedInheritor.isReferenceAssignableTo())
                }
            }
        }

        if (Relation.ANY == relation && !satisfies(signature)) {
            return false;
        }

        if (Relation.ALSO == relation && signature.satisfies(this)) {
            return false;
        }

        return Option.of(commonSubject);
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

        if (!subject.equals(signature.getSubject())) {
            return false;
        }

        if (!Arrays.equals(generics, signature.getGenerics())) {
            return false;
        }

        return relation == signature.getRelation();
    }

    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + Arrays.hashCode(generics);
        result = 31 * result + relation.hashCode();
        return result;
    }

    @Override
    public V getSubject() {
        return subject;
    }

    public Relation getRelation() {
        return relation;
    }

    public Signature[] getGenerics() {
        return generics;
    }

    @Override
    public Option<Signature> getParent() {
        return parent;
    }

}
