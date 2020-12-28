package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

import java.util.Arrays;

abstract class AbstractSignature<V> implements Signature {

    private final Option<Signature> parent;
    private final V subject;
    private final Signature[] generics;
    private final Relation relation;
    private final Snippetable source;

    protected AbstractSignature(@Nullable Signature parent, V subject, Signature[] generics, Relation relation, Snippetable source) {
        this.parent = Option.of(parent);
        this.subject = subject;
        this.generics = generics;
        this.relation = relation;
        this.source = source;
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
    public Result<? extends Signature, String> apply(Signed context) {
        return null;
    }

    @Override
    public boolean isAssignableFrom(Signed from) {
        if (from == null) {
            return true; // TODO: Remove nulls
        }
        
        return from.getSignature().merge(this).isOk();
    }

    @Override
    public Result<? extends Signature, String> merge(Signed inheritor) {
        if (generics.length != inheritor.getSignature().getGenerics().length) {
            return Result.error("Invalid amount of parameters in generic signature");
        }

        return SignatureUtils.merge(this, inheritor.getSignature());
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
    public Type getKnownType() {
        return toType();
    }

    @Override
    public V getSubject() {
        return subject;
    }

    @Override
    public Snippet getSource() {
        return source.toSnippet();
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
