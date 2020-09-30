package org.panda_lang.language.interpreter.pattern.functional.verifiers;

import org.panda_lang.language.interpreter.pattern.functional.Verifier;
import org.panda_lang.language.interpreter.token.Snippetable;
import org.panda_lang.language.interpreter.token.SynchronizedSource;
import org.panda_lang.language.interpreter.token.Token;
import org.panda_lang.utilities.commons.collection.Pair;

import java.util.List;

public final class TokenVerifier implements Verifier<Snippetable> {

    private final Token token;

    public TokenVerifier(Token token) {
        this.token = token;
    }

    @Override
    public boolean verify(List<Pair<String, Object>> results, SynchronizedSource source, Snippetable content) {
        return source.getCurrent()
                .map(tokenInfo ->  token.equals(tokenInfo.getToken()))
                .orElseGet(false);
    }

}