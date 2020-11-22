package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.type.Reference;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.utilities.commons.text.Joiner;

public final class TypedSignature extends AbstractSignature<Reference> {

    public TypedSignature(@Nullable Signature parent, Reference subject, Signature[] generics, Relation relation, Snippetable source) {
        super(parent, subject, generics, relation, source);
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

    @Override
    public String toString() {
        return getRelation() + " : " + getSubject() + "<" + Joiner.on(", ").join(getGenerics()) + ">";
    }

}
