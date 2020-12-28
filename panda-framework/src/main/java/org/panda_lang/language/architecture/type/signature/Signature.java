package org.panda_lang.language.architecture.type.signature;

import org.panda_lang.language.architecture.type.Typed;
import org.panda_lang.language.interpreter.token.Snippet;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

public interface Signature extends Signed, Typed {

    Signature apply(Signed context);

    boolean isAssignableFrom(Signed inheritor);

    Result<? extends Signature, String> merge(Signed inheritor);

    default Option<Pair<GenericSignature, Signature>> findGeneric(GenericSignature identifier) {
        return findGeneric(identifier.getLocalIdentifier());
    }

    Option<Pair<GenericSignature, Signature>> findGeneric(String identifier);

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
