package org.panda_lang.panda.framework.design.interpreter.pattern.readers;

import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.ExpressionParser;
import org.panda_lang.panda.framework.language.interpreter.parser.expression.subparsers.DefaultSubparsers;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.wildcard.reader.WildcardReader;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.utilities.commons.ArrayUtils;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.Arrays;
import java.util.Collection;

class ExpressionWildcardReader implements WildcardReader {

    private static final ExpressionParser PARSER = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers());

    @Override
    public boolean match(String data) {
        return data.startsWith("expression");
    }

    @Override
    public Tokens read(String data, TokenDistributor distributor) {
        String[] datum = StringUtils.splitFirst(data, " ");

        if (ArrayUtils.isEmpty(datum)) {
            Tokens tokens = PARSER.read(distributor.currentSubSource());

            if (tokens == null) {
                return null;
            }

            distributor.next(tokens.size());
            return tokens;
        }

        String condition = datum[1];
        Collection<String> names = convert(StringUtils.splitFirst(condition, " ")[1]);

        boolean exclude = condition.startsWith("exclude");
        ExpressionParser parser;

        if (exclude) {
            parser = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers());
            parser.removeSubparsers(names);
        }
        else {
            parser = new ExpressionParser(DefaultSubparsers.Instances.getDefaultSubparsers(names));
        }

        Tokens source = parser.read(distributor.currentSubSource());

        if (source == null) {
            return null;
        }

        distributor.next(source.size());
        return source;
    }

    private Collection<String> convert(String elements) {
        return Arrays.asList(elements.split(","));
    }

}
