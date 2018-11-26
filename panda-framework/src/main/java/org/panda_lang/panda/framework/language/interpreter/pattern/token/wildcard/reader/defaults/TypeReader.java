package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.defaults;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.design.interpreter.token.TokenType;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.language.interpreter.token.PandaTokens;

class TypeReader implements WildcardReader {

    @Override
    public boolean match(String data) {
        return data.startsWith("type");
    }

    @Override
    public @Nullable Tokens read(String data, TokenDistributor distributor) {
        TokenRepresentation type = distributor.next();

        if (type.getToken().getType() != TokenType.UNKNOWN) {
            return null;
        }

        return new PandaTokens(type);
    }

}
