package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;

public final class  GenericSignature extends AbstractSignature<Pair<String, Signature>> {

    public GenericSignature(@Nullable Signature parent, String identifier, Signature signature, Signature[] generics, Relation relation) {
        super(parent, new Pair<>(identifier, signature), generics, relation);
    }

    public String getLocalIdentifier() {
        return getSubject().getKey();
    }

    public boolean isWildcard() {
        return getLocalIdentifier().equals("*") || getSubject().getValue().getRelation().isWildcard();
    }

    public Option<Signature> getAny() {
        Signature signature = getSubject().getValue();
        return Option.when(Relation.ANY == signature.getRelation(), signature);
    }

    public Option<Signature> getAlso() {
        Signature signature = getSubject().getValue();
        return Option.when(Relation.ALSO == signature.getRelation(), signature);
    }

}
