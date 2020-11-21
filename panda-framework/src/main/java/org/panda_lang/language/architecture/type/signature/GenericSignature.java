package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;

public final class GenericSignature extends AbstractSignature<Pair<String, Signature>> {

    public GenericSignature(@Nullable Signature parent, String identifier, Signature signature, Signature[] generics, Relation relation, Snippetable source) {
        super(parent, new Pair<>(identifier, signature), generics, relation, source);
    }

    @Override
    public boolean isGeneric() {
        return true;
    }

    @Override
    public GenericSignature toGeneric() {
        return this;
    }

    public boolean isWildcard() {
        return getLocalIdentifier().equals("*") || getSubject().getValue().getRelation().isWildcard();
    }

    public String getLocalIdentifier() {
        return getSubject().getKey();
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
