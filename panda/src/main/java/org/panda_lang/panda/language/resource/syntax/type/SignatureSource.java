package org.panda_lang.panda.language.resource.syntax.type;

import org.panda_lang.language.interpreter.token.TokenInfo;

import java.util.List;

public final class SignatureSource {

    private final TokenInfo name;
    private final List<SignatureSource> generics;

    public SignatureSource(TokenInfo name, List<SignatureSource> generics) {
        this.name = name;
        this.generics = generics;
    }

    public List<SignatureSource> getGenerics() {
        return generics;
    }

    public TokenInfo getName() {
        return name;
    }

}
