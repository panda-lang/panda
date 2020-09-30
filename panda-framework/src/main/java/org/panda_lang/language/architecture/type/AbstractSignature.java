package org.panda_lang.language.architecture.type;

import org.panda_lang.utilities.commons.function.Option;

public final class AbstractSignature {

    private final String identifier;
    private final Option<Signature> extendsSignature;
    private final Option<Signature> supersSignature;

    AbstractSignature(String identifier, Option<Signature> extendsSignature, Option<Signature> supersSignature) {
        this.identifier = identifier;
        this.extendsSignature = extendsSignature;
        this.supersSignature = supersSignature;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Option<Signature> getExtendsSignature() {
        return extendsSignature;
    }

    public Option<Signature> getSupersSignature() {
        return supersSignature;
    }

    public static AbstractSignature of(String identifier) {
        return new AbstractSignature(identifier, Option.none(), Option.none());
    }

    public static AbstractSignature ofExtends(String identifier, Signature extendsSignature) {
        return new AbstractSignature(identifier, Option.of(extendsSignature), Option.none());
    }

    public static AbstractSignature ofSuper(String identifier, Signature superSignature) {
        return new AbstractSignature(identifier, Option.none(), Option.of(superSignature));
    }

}
