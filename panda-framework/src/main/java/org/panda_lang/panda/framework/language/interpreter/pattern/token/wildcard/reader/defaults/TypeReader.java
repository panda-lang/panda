package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.defaults;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.extractor.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.WildcardReader;

public class TypeReader implements WildcardReader {

    @Override
    public boolean match(String data) {
        return data.startsWith("type");
    }

    @Override
    public Tokens read(String data, TokenDistributor distributor) {
        return null;
    }

}
