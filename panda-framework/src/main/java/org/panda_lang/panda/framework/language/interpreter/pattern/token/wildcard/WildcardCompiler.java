package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.interpreter.token.Tokens;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.language.interpreter.token.distributors.TokenDistributor;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition.WildcardConditionCompiler;
import org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.reader.WildcardReaderCompiler;
import org.panda_lang.panda.utilities.commons.StringUtils;

public class WildcardCompiler {

    private final TokenPattern pattern;
    private final WildcardConditionCompiler conditionCompiler;
    private final WildcardReaderCompiler readerCompiler;

    public WildcardCompiler(TokenPattern pattern) {
        this.pattern = pattern;
        this.conditionCompiler = new WildcardConditionCompiler(pattern);
        this.readerCompiler = new WildcardReaderCompiler(pattern);
    }

    public @Nullable Tokens compile(String data, TokenDistributor distributor) {
        String[] entry = StringUtils.splitFirst(data, " ");
        String type = entry[0];
        String value = entry[1];

        if (type.equals("condition")) {
            return conditionCompiler.extract(value, distributor);
        }

        if (type.equals("reader")) {
            return readerCompiler.extract(value, distributor);
        }

        throw new TokenPatternWildcardException("Unknown condition: " + type);
    }

}
