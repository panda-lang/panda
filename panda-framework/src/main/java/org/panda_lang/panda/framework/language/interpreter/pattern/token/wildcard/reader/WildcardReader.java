package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;

public interface WildcardReader {

    boolean match(String data);

    @Nullable Tokens read(String data, TokenDistributor distributor);

}
