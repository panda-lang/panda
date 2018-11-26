package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenDistributor;

public interface WildcardReader {

    @Nullable Tokens read(TokenDistributor distributor);

}
