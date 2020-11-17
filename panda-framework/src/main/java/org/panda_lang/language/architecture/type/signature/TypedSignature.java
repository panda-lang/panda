package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.Type;

public final class TypedSignature extends AbstractSignature<Reference> {

    public TypedSignature(@Nullable Signature parent, Reference subject, Signature[] generics, Relation relation) {
        super(parent, subject, generics, relation);
    }

    public boolean isReferenceAssignableTo(Reference reference) {
        return getSubject().getType().get().isAssignableFrom(reference.getType().get());
    }

    @Override
    public boolean isTyped() {
        return true;
    }

    @Override
    public TypedSignature toTyped() {
        return this;
    }

    public Type fetchType() {
        return getReference().fetchType();
    }

    public Reference getReference() {
        return getSubject();
    }

}
