package org.panda_lang.language.architecture.type.signature;

import org.panda_lang.language.architecture.type.Typed;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

public interface Signature extends Signed, Typed {

    boolean isAssignableFrom(Signature inheritor);

    Result<? extends Signature, String> merge(Signature inheritor);

    default Option<GenericSignature> findGeneric(GenericSignature identifier) {
        return findGeneric(identifier.getLocalIdentifier());
    }

    Option<GenericSignature> findGeneric(String identifier);

    boolean isGeneric();

    GenericSignature toGeneric();

    boolean isTyped();

    TypedSignature toTyped();

    Snippet getSource();

    Signature[] getGenerics();

    Relation getRelation();

    Object getSubject();

    Option<Signature> getParent();

    @Override
    default Signature getSignature() {
        return this;
    }

}
