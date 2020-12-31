package org.panda_lang.language.architecture.type.signature;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.language.architecture.module.TypeLoader;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.utilities.commons.collection.Pair;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.text.Joiner;

public final class GenericSignature extends AbstractSignature<Pair<String, Signature>> {

    private final TypeLoader typeLoader;

    public GenericSignature(TypeLoader typeLoader, @Nullable Signature parent, String identifier, Signature signature, Signature[] generics, Relation relation, Snippetable source) {
        super(parent, new Pair<>(identifier, signature), generics, relation, source);
        this.typeLoader = typeLoader;
    }

    @Override
    public Signature apply(Signed context) {
        Option<Pair<GenericSignature, Signature>> result = context.getSignature().findGeneric(this);

        if (result.isPresent()) {
            return result.get().getValue();
        }

        return new GenericSignature(typeLoader, getParent().getOrNull(), getLocalIdentifier(), getSubject().getValue(), applyGenerics(context), getRelation(), getSource());
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
        return getLocalIdentifier().equals("*");
    }

    public boolean hasWildcardDescription() {
        return getSubject().getValue() != null;
    }

    public Option<Signature> getWildcardDescription() {
        return Option.of(getSubject().getValue());
    }

    public String getLocalIdentifier() {
        return getSubject().getKey();
    }

    public Option<Signature> getAny() {
        Signature signature = getSubject().getValue();
        return Option.when(signature != null && Relation.ANY == signature.getRelation(), signature);
    }

    public Option<Signature> getAlso() {
        Signature signature = getSubject().getValue();
        return Option.when(signature != null && Relation.ALSO == signature.getRelation(), signature);
    }

    @Override
    public Type toType() {
        return getAny().map(Signature::toType)
                .orElse(() -> getAlso().map(Signature::toType))
                .orElseGet(() -> typeLoader.requireType("panda::Object"));
    }

    @Override
    public String toString() {
        return getRelation() + " : abstract " + getLocalIdentifier() + "<" + Joiner.on(", ").join(getGenerics()) + ">";
    }

}
