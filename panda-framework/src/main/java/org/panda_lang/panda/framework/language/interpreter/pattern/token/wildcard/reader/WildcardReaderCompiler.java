package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenDistributor;

public class WildcardReaderCompiler {

    private final TokenPattern pattern;

    public WildcardReaderCompiler(TokenPattern pattern) {
        this.pattern = pattern;
    }

    public @Nullable Tokens extract(String data, TokenDistributor distributor) {
        return null;
    }

}
