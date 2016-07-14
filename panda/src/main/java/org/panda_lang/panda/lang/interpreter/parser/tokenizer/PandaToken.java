package org.panda_lang.panda.lang.interpreter.parser.tokenizer;

import org.panda_lang.core.interpreter.parser.tokenizer.Token;

public class PandaToken implements Token {

    private final String token;

    public PandaToken(String token) {
        this.token = token;
    }

    @Override
    public String getToken() {
        return null;
    }

}
