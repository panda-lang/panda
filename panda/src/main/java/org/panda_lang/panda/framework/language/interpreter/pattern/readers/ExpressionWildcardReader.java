package org.panda_lang.panda.framework.language.interpreter.pattern.readers;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.implementation.general.expression.updated.subparsers.DefaultSubparsers;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.WildcardReader;

class ExpressionWildcardReader implements WildcardReader {

    private static final ExpressionParser PARSER = new ExpressionParser(DefaultSubparsers.getDefaultSubparsers());

    @Override
    public boolean match(String data) {
        return data.startsWith("expression");
    }

    @Override
    public Tokens read(String data, TokenDistributor distributor) {
        return PARSER.read(distributor.currentSubSource());
    }

}
